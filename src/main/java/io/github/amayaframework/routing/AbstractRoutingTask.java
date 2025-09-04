package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.http.HttpVersion;
import io.github.amayaframework.router.Router;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for routing tasks responsible for dispatching HTTP requests
 * to the appropriate route handler.
 * <p>
 * Integrates with a {@link Router} to resolve routes, applies parameter parsing,
 * and handles common HTTP semantics such as 404 (Not Found),
 * 405 (Method Not Allowed), and automatic {@code OPTIONS} responses.
 * <p>
 * Subclasses may extend this to implement framework-specific routing logic.
 */
public abstract class AbstractRoutingTask implements TaskConsumer<HttpContext> {

    /**
     * Standard {@code Allow} response header name.
     */
    protected static final String ALLOW_HEADER = "Allow";

    /**
     * Standard {@code Cache-Control} response header name.
     */
    protected static final String CACHE_CONTROL_HEADER = "Cache-Control";

    protected final Router<MethodMap> router;
    protected final ParamParser parser;
    protected final boolean handleOptions;
    protected final String cacheControl;

    /**
     * Creates a new routing task.
     *
     * @param router        the underlying router used to resolve paths
     * @param parser        the parameter parser to apply (may be {@code null})
     * @param handleOptions whether {@code OPTIONS} requests should be handled automatically
     * @param cacheControl  optional value for the {@code Cache-Control} response header
     */
    protected AbstractRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        this.router = router;
        this.parser = parser;
        this.handleOptions = handleOptions;
        this.cacheControl = cacheControl;
    }

    /**
     * Builds the value of the {@code Allow} header from a set of allowed methods.
     * Ensures that {@code OPTIONS} is always included.
     *
     * @param allowed the set of allowed methods
     * @return a comma-separated list of methods suitable for the {@code Allow} header
     */
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

    /**
     * Adds the configured {@code Cache-Control} header to the response, if present.
     *
     * @param response the response to update
     */
    protected void applyCacheControl(HttpResponse response) {
        if (cacheControl != null) {
            response.header(CACHE_CONTROL_HEADER, cacheControl);
        }
    }

    /**
     * Sends a successful {@code 204 No Content} response for an {@code OPTIONS} request.
     *
     * @param response the response to send to
     * @param allowed  the set of allowed methods
     */
    protected void sendOptionsResponse(HttpResponse response, Set<HttpMethod> allowed) {
        response.status(HttpCode.NO_CONTENT);
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
    }

    /**
     * Sends a {@code 405 Method Not Allowed} response with an {@code Allow} header.
     *
     * @param response the response to send to
     * @param allowed  the set of allowed methods
     * @throws IOException if the response cannot be written
     */
    protected void sendMethodNotAllowed(HttpResponse response, Set<HttpMethod> allowed) throws IOException {
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
        response.sendError(HttpCode.METHOD_NOT_ALLOWED);
    }

    /**
     * Sends a successful {@code 204 No Content} response for an {@code OPTIONS} request.
     *
     * @param response the response to send to
     * @param allowed  the set of allowed methods
     */
    protected CompletableFuture<Void> sendOptionsResponseAsync(HttpResponse response, Set<HttpMethod> allowed) {
        sendOptionsResponse(response, allowed);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Sends a {@code 405 Method Not Allowed} response with an {@code Allow} header.
     *
     * @param response the response to send to
     * @param allowed  the set of allowed methods
     */
    protected CompletableFuture<Void> sendMethodNotAllowedAsync(HttpResponse response, Set<HttpMethod> allowed) {
        response.header(ALLOW_HEADER, generateAllowHeader(allowed));
        applyCacheControl(response);
        try {
            response.sendError(HttpCode.METHOD_NOT_ALLOWED);
            return CompletableFuture.completedFuture(null);
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Executes a resolved handler synchronously.
     *
     * @param handler the handler to run
     * @param context the current HTTP context
     * @throws Throwable if the handler throws
     */
    protected void runHandler(Task<HttpContext> handler, HttpContext context) throws Throwable {
        handler.run(context);
    }

    /**
     * Executes a resolved handler asynchronously.
     *
     * @param handler the handler to run
     * @param context the current HTTP context
     * @return a future completed when the handler finishes
     */
    protected CompletableFuture<Void> runHandlerAsync(Task<HttpContext> handler, HttpContext context) {
        return handler.runAsync(context);
    }

    /**
     * Entry point for synchronous execution. Resolves the route and dispatches to the appropriate handler.
     * <p>
     * Sends 404 if the path is not found, 405 if the method is not supported,
     * or executes the matching handler otherwise.
     */
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
        // Fast check for no parser
        if (parser == null) {
            runHandler(handler, context);
            return;
        }
        try {
            parser.process(request, data);
        } catch (IllegalParamException e) {
            response.sendError(HttpCode.BAD_REQUEST, e.getMessage());
            return;
        }
        // Finally, run handler
        runHandler(handler, context);
    }

    /**
     * Entry point for asynchronous execution. Resolves the route and dispatches to the appropriate handler.
     * <p>
     * Behavior is the same as {@link #run(HttpContext, Task)}, but non-blocking.
     */
    @Override
    public CompletableFuture<Void> runAsync(HttpContext context, Task<HttpContext> task) {
        var request = context.request();
        var response = context.response();
        // Process route
        var found = router.process(request.path(), request::pathSegments);
        // If there are no handlers, return 404
        if (found == null) {
            try {
                response.sendError(HttpCode.NOT_FOUND, "Path not found");
                return CompletableFuture.completedFuture(null);
            } catch (IOException e) {
                return CompletableFuture.failedFuture(e);
            }
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
                try {
                    response.sendError(HttpCode.NOT_FOUND, "Method " + method + " not allowed");
                    return CompletableFuture.completedFuture(null);
                } catch (IOException e) {
                    return CompletableFuture.failedFuture(e);
                }
            }
            // Send 405
            return sendMethodNotAllowedAsync(response, map.methods());
        }
        // Process path and query parameters
        var data = found.getData();
        // Fast check for no parser
        if (parser == null) {
            return runHandlerAsync(handler, context);
        }
        try {
            parser.process(request, data);
        } catch (IllegalParamException e) {
            try {
                response.sendError(HttpCode.BAD_REQUEST, e.getMessage());
                return CompletableFuture.completedFuture(null);
            } catch (IOException ioe) {
                return CompletableFuture.failedFuture(ioe);
            }
        }
        // Finally, run handler
        return runHandlerAsync(handler, context);
    }
}
