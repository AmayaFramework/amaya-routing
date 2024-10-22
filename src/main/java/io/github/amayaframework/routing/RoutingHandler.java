package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import com.github.romanqed.jfunc.Runnable2;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.path.Parameter;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;
import io.github.amayaframework.router.Router;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class RoutingHandler implements Runnable2<HttpContext, Runnable1<HttpContext>> {
    private final Router<Map<HttpMethod, Runnable1<HttpContext>>> router;
    private final FilterSet filters;

    RoutingHandler(Router<Map<HttpMethod, Runnable1<HttpContext>>> router, FilterSet filters) {
        this.router = router;
        this.filters = filters;
    }

    private static String getBadRequestMessage(String type, Parameter parameter, Object value, String reason) {
        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
    }

    private boolean processPathParameters(HttpRequest request,
                                          HttpResponse response,
                                          List<PathParameter> parameters) throws IOException {
        if (parameters == null || parameters.isEmpty()) {
            return false;
        }
        var segments = request.getPathSegments();
        var map = request.getPathParameters();
        for (var parameter : parameters) {
            var raw = segments.get(parameter.getIndex());
            var name = parameter.getName();
            var type = parameter.getType();
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
                response.sendError(
                        HttpCode.BAD_REQUEST,
                        getBadRequestMessage("Path", parameter, raw, e.getMessage())
                );
                return true;
            }
        }
        return false;
    }

    private boolean processQueryParameters(HttpRequest request,
                                           HttpResponse response,
                                           List<QueryParameter> parameters) throws IOException {
        if (parameters == null || parameters.isEmpty()) {
            return false;
        }
        var queries = request.getQueryParameters();
        for (var parameter : parameters) {
            var raw = queries.get(parameter.getName());
            if (raw == null) {
                if (parameter.isRequired() == Boolean.TRUE) {
                    response.sendError(HttpCode.BAD_REQUEST, "Missing required query parameter " + parameter);
                    return true;
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
                response.sendError(
                        HttpCode.BAD_REQUEST,
                        getBadRequestMessage("Query", parameter, raw, e.getMessage())
                );
                return true;
            }
        }
        return false;
    }

    @Override
    public void run(HttpContext context, Runnable1<HttpContext> next) throws Throwable {
        var request = context.getRequest();
        var response = context.getResponse();
        var path = request.getPath();
        // Process route
        var found = router.process(path, request::getPathSegments);
        // If there is no handlers, return 404
        if (found == null) {
            response.sendError(HttpCode.NOT_FOUND, "No handlers for path " + path + " found");
            return;
        }
        // Try to get handler for http method, otherwise return 405
        var method = request.getMethod();
        var handler = found.getValue().get(method);
        if (handler == null) {
            response.sendError(
                    HttpCode.METHOD_NOT_ALLOWED,
                    "Method " + method + " not allowed for path " + path
            );
            return;
        }
        // Process path and query parameters
        var data = found.getData();
        if (filters == null || data == null) {
            handler.run(context);
            next.run(context);
            return;
        }
        // If path params failed, return 400
        if (processPathParameters(request, response, data.getPathParameters())) {
            return;
        }
        // If query params failed, return 400
        if (processQueryParameters(request, response, data.getQueryParameters())) {
            return;
        }
        // Finally, run handler
        handler.run(context);
        next.run(context);
    }
}
