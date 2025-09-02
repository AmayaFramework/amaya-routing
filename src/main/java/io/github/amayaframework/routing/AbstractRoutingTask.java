package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import com.github.romanqed.jsync.Futures;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.http.HttpVersion;
import io.github.amayaframework.path.Parameter;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;
import io.github.amayaframework.router.Router;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractRoutingTask implements TaskConsumer<HttpContext> {
    protected static final String ALLOW_HEADER = "Allow";
    protected static final String CACHE_CONTROL_HEADER = "Cache-Control";

    protected final Router<MethodMap> router;
    protected final FilterSet filters;
    protected final boolean handleOptions;
    protected final String cacheControl;

    protected AbstractRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions, String cacheControl) {
        this.router = router;
        this.filters = filters;
        this.handleOptions = handleOptions;
        this.cacheControl = cacheControl;
    }

    protected String getBadRequestMessage(String type, Parameter parameter, Object value, String reason) {
        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
    }

    protected String generateAllowHeader(Set<HttpMethod> allowed) {
        var iterator = allowed.iterator();
        var builder = new StringBuilder(iterator.next().getName());
        while (iterator.hasNext()) {
            builder.append(", ").append(iterator.next().getName());
        }
        if (!allowed.contains(HttpMethod.OPTIONS)) {
            builder.append(", OPTIONS");
        }
        return builder.toString();
    }

    protected String processPathParams(HttpRequest request, List<PathParameter> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        var segments = request.pathSegments();
        var map = request.pathParams();
        for (var param : params) {
            var raw = segments.get(param.getIndex());
            var name = param.getName();
            var type = param.getType();
            if (type == null) {
                map.put(name, raw);
                continue;
            }
            var filter = filters.get(type);
            if (filter == null) {
                map.put(name, raw);
                continue;
            }
            try {
                var object = filter.process(raw);
                map.put(name, object);
            } catch (Throwable e) {
                return getBadRequestMessage("Path", param, raw, e.getMessage());
            }
        }
        return null;
    }

    protected String processQueryParams(HttpRequest request, List<QueryParameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        var queries = request.queryParams();
        for (var parameter : parameters) {
            var raw = queries.get(parameter.getName());
            if (raw == null) {
                if (parameter.isRequired() == Boolean.TRUE) {
                    return "Missing required query parameter " + parameter;
                }
                continue;
            }
            var type = parameter.getType();
            if (type == null || raw.isEmpty()) {
                continue;
            }
            var filter = filters.get(type);
            if (filter == null) {
                continue;
            }
            try {
                var size = raw.size();
                for (var i = 0; i < size; ++i) {
                    var string = (String) raw.get(i);
                    raw.set(i, filter.process(string));
                }
            } catch (Throwable e) {
                return getBadRequestMessage("Query", parameter, raw, e.getMessage());
            }
        }
        return null;
    }

    protected void applyCacheControl(HttpResponse response) {
        if (cacheControl != null) {
            response.header(CACHE_CONTROL_HEADER, cacheControl);
        }
    }

    protected void sendOptionsResponse(HttpResponse response, Set<HttpMethod> allowed) {
        response.status(HttpCode.NO_CONTENT);
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
    }

    protected void sendMethodNotAllowed(HttpResponse response, Set<HttpMethod> allowed) throws IOException {
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
        response.sendError(HttpCode.METHOD_NOT_ALLOWED);
    }

    protected CompletableFuture<Void> sendOptionsResponseAsync(HttpResponse response, Set<HttpMethod> allowed) {
        sendOptionsResponse(response, allowed);
        return CompletableFuture.completedFuture(null);
    }

    protected CompletableFuture<Void> sendMethodNotAllowedAsync(HttpResponse response, Set<HttpMethod> allowed) {
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
        return Futures.run(() -> response.sendError(HttpCode.METHOD_NOT_ALLOWED));
    }

    protected void runHandler(Task<HttpContext> handler, HttpContext context) throws Throwable {
        handler.run(context);
    }

    protected CompletableFuture<Void> runHandlerAsync(Task<HttpContext> handler, HttpContext context) {
        return handler.runAsync(context);
    }

    @Override
    public void run(HttpContext context, Task<HttpContext> next) throws Throwable {
        var request = context.request();
        var response = context.response();
        // Process route
        var found = router.process(request.path(), request::pathSegments);
        // If there are no handlers, return 404
        if (found == null) {
            response.sendError(HttpCode.NOT_FOUND, "Path not found");
            return;
        }
        // Try to get handler for http method, otherwise return 405
        var method = request.method();
        var map = found.getValue();
        var handler = map.get(method);
        if (handler == null) {
            if (handleOptions && method == HttpMethod.OPTIONS) {
                // Handle options request
                sendOptionsResponse(response, map.methods());
            } else if (request.httpVersion() == HttpVersion.HTTP_1_0) {
                // Send 404 as HTTP/1.0 does not support 405
                response.sendError(HttpCode.NOT_FOUND, "Method " + method + " not allowed");
            } else {
                // Send 405
                sendMethodNotAllowed(response, map.methods());
            }
            return;
        }
        // Process path and query parameters
        var data = found.getData();
        // Fast check for no-known params/no filters
        if (filters == null || data == null) {
            runHandler(handler, context);
            return;
        }
        String message;
        // If path params failed, return 400
        if ((message = processPathParams(request, data.getPathParams())) != null) {
            response.sendError(HttpCode.BAD_REQUEST, message);
            return;
        }
        // If query params failed, return 400
        if ((message = processQueryParams(request, data.getQueryParams())) != null) {
            response.sendError(HttpCode.BAD_REQUEST, message);
            return;
        }
        // Finally, run handler
        runHandler(handler, context);
    }

    @Override
    public CompletableFuture<Void> runAsync(HttpContext context, Task<HttpContext> task) {
        var request = context.request();
        var response = context.response();
        // Process route
        var found = router.process(request.path(), request::pathSegments);
        // If there are no handlers, return 404
        if (found == null) {
            return Futures.run(() -> response.sendError(HttpCode.NOT_FOUND, "Path not found"));
        }
        // Try to get handler for http method, otherwise return 405
        var method = request.method();
        var map = found.getValue();
        var handler = map.get(method);
        if (handler == null) {
            // Handle options request
            if (handleOptions && method == HttpMethod.OPTIONS) {
                return sendOptionsResponseAsync(response, map.methods());
            }
            // Send 404 as HTTP/1.0 does not support 405
            if (request.httpVersion() == HttpVersion.HTTP_1_0) {
                return Futures.run(() ->
                        response.sendError(HttpCode.NOT_FOUND, "Method " + method + " not allowed")
                );
            }
            // Send 405
            return sendMethodNotAllowedAsync(response, map.methods());
        }
        // Process path and query parameters
        var data = found.getData();
        // Fast check for no-known params/no filters
        if (filters == null || data == null) {
            return runHandlerAsync(handler, context);
        }
        var pathMessage = processPathParams(request, data.getPathParams());
        // If path params failed, return 400
        if (pathMessage != null) {
            return Futures.run(() -> response.sendError(HttpCode.BAD_REQUEST, pathMessage));
        }
        var queryMessage = processQueryParams(request, data.getQueryParams());
        // If query params failed, return 400
        if (queryMessage != null) {
            return Futures.run(() -> response.sendError(HttpCode.BAD_REQUEST, queryMessage));
        }
        // Finally, run handler
        return runHandlerAsync(handler, context);
    }
}
