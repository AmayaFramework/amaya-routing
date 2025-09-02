//package io.github.amayaframework.routing;
//
//import com.github.romanqed.jfunc.Runnable1;
//import com.github.romanqed.jfunc.Runnable2;
//import io.github.amayaframework.context.HttpContext;
//import io.github.amayaframework.context.HttpRequest;
//import io.github.amayaframework.context.HttpResponse;
//import io.github.amayaframework.filter.FilterSet;
//import io.github.amayaframework.http.HttpCode;
//import io.github.amayaframework.http.HttpMethod;
//import io.github.amayaframework.http.HttpVersion;
//import io.github.amayaframework.path.Parameter;
//import io.github.amayaframework.path.PathParameter;
//import io.github.amayaframework.path.QueryParameter;
//import io.github.amayaframework.router.Router;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public final class RoutingHandler implements Runnable2<HttpContext, Runnable1<HttpContext>> {
//    private static final String ALLOW_HEADER = "Allow";
//    private static final String CACHE_CONTROL_HEADER = "Cache-Control";
//    private final Router<Map<HttpMethod, Runnable1<HttpContext>>> router;
//    private final FilterSet filters;
//    private final boolean handleOptions;
//    private final String cacheControl;
//
//    public RoutingHandler(Router<Map<HttpMethod, Runnable1<HttpContext>>> router,
//                          FilterSet filters,
//                          boolean handleOptions,
//                          String cacheControl) {
//        this.router = router;
//        this.filters = filters;
//        this.handleOptions = handleOptions;
//        this.cacheControl = cacheControl;
//    }
//
//    public RoutingHandler(Router<Map<HttpMethod, Runnable1<HttpContext>>> router,
//                          FilterSet filters,
//                          boolean handleOptions) {
//        this(router, filters, handleOptions, null);
//    }
//
//    public RoutingHandler(Router<Map<HttpMethod, Runnable1<HttpContext>>> router, FilterSet filters) {
//        this(router, filters, true, null);
//    }
//
//    private static String getBadRequestMessage(String type, Parameter parameter, Object value, String reason) {
//        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
//    }
//
//    private static String generateAllowHeader(Set<HttpMethod> allowed) {
//        var iterator = allowed.iterator();
//        var builder = new StringBuilder(iterator.next().getName());
//        while (iterator.hasNext()) {
//            builder.append(", ").append(iterator.next().getName());
//        }
//        if (!allowed.contains(HttpMethod.OPTIONS)) {
//            builder.append(", OPTIONS");
//        }
//        return builder.toString();
//    }
//
//    private boolean processPathParameters(HttpRequest request,
//                                          HttpResponse response,
//                                          List<PathParameter> parameters) throws IOException {
//        if (parameters == null || parameters.isEmpty()) {
//            return false;
//        }
//        var segments = request.getPathSegments();
//        var map = request.getPathParameters();
//        for (var parameter : parameters) {
//            var raw = segments.get(parameter.getIndex());
//            var name = parameter.getName();
//            var type = parameter.getType();
//            if (type == null) {
//                map.put(name, raw);
//                continue;
//            }
//            var filter = filters.get(type);
//            if (filter == null) {
//                map.put(name, raw);
//                continue;
//            }
//            try {
//                var object = filter.process(raw);
//                map.put(name, object);
//            } catch (Throwable e) {
//                response.sendError(
//                        HttpCode.BAD_REQUEST,
//                        getBadRequestMessage("Path", parameter, raw, e.getMessage())
//                );
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean processQueryParameters(HttpRequest request,
//                                           HttpResponse response,
//                                           List<QueryParameter> parameters) throws IOException {
//        if (parameters == null || parameters.isEmpty()) {
//            return false;
//        }
//        var queries = request.getQueryParameters();
//        for (var parameter : parameters) {
//            var raw = queries.get(parameter.getName());
//            if (raw == null) {
//                if (parameter.isRequired() == Boolean.TRUE) {
//                    response.sendError(HttpCode.BAD_REQUEST, "Missing required query parameter " + parameter);
//                    return true;
//                }
//                continue;
//            }
//            var type = parameter.getType();
//            if (type == null || raw.isEmpty()) {
//                continue;
//            }
//            var filter = filters.get(type);
//            if (filter == null) {
//                continue;
//            }
//            try {
//                var size = raw.size();
//                for (var i = 0; i < size; ++i) {
//                    var string = (String) raw.get(i);
//                    raw.set(i, filter.process(string));
//                }
//            } catch (Throwable e) {
//                response.sendError(
//                        HttpCode.BAD_REQUEST,
//                        getBadRequestMessage("Query", parameter, raw, e.getMessage())
//                );
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void sendMethodNotAllowed(HttpResponse response, Set<HttpMethod> allowed) throws IOException {
//        response.setHeader(ALLOW_HEADER, generateAllowHeader(allowed));
//        response.sendError(HttpCode.METHOD_NOT_ALLOWED);
//    }
//
//    private void sendOptionsResponse(HttpResponse response, Set<HttpMethod> allowed) {
//        response.setStatus(HttpCode.NO_CONTENT);
//        response.setHeader(ALLOW_HEADER, generateAllowHeader(allowed));
//        if (cacheControl != null) {
//            response.setHeader(CACHE_CONTROL_HEADER, cacheControl);
//        }
//    }
//
//    @Override
//    public void run(HttpContext context, Runnable1<HttpContext> next) throws Throwable {
//        var request = context.getRequest();
//        var response = context.getResponse();
//        // Process route
//        var found = router.process(request.getPath(), request::getPathSegments);
//        // If there is no handlers, return 404
//        if (found == null) {
//            response.sendError(HttpCode.NOT_FOUND, "Path not found");
//            return;
//        }
//        // Try to get handler for http method, otherwise return 405
//        var method = request.getMethod();
//        var map = found.getValue();
//        var handler = map.get(method);
//        if (handler == null) {
//            if (handleOptions && method == HttpMethod.OPTIONS) {
//                // Handle options request
//                sendOptionsResponse(response, map.keySet());
//            } else if (request.getHttpVersion() == HttpVersion.HTTP_1_0) {
//                // HTTP/1.0 does not support 405 code
//                response.sendError(HttpCode.NOT_FOUND, "Method " + method + " not allowed");
//            } else {
//                // Send not allowed
//                sendMethodNotAllowed(response, map.keySet());
//            }
//            return;
//        }
//        // Process path and query parameters
//        var data = found.getData();
//        if (filters == null || data == null) {
//            handler.run(context);
//            next.run(context);
//            return;
//        }
//        // If path params failed, return 400
//        if (processPathParameters(request, response, data.getPathParameters())) {
//            return;
//        }
//        // If query params failed, return 400
//        if (processQueryParameters(request, response, data.getQueryParameters())) {
//            return;
//        }
//        // Finally, run handler
//        handler.run(context);
//        next.run(context);
//    }
//}
