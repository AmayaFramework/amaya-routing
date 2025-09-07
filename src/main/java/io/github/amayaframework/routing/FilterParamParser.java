package io.github.amayaframework.routing;

import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.path.Parameter;
import io.github.amayaframework.path.PathData;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;

import java.util.List;
import java.util.Map;

/**
 * A {@link ParamParser} implementation that applies {@link FilterSet filters}
 * and optional URL decoding to request path and query parameters.
 * <p>
 * This parser supports:
 * <ul>
 *   <li>Decoding path segments via a {@link UrlDecoder}, controlled by {@code decodePath}.</li>
 *   <li>Decoding query parameter names and values, controlled by {@code decodeQuery}.</li>
 *   <li>Type conversion and validation of parameters using the configured {@link FilterSet}.</li>
 * </ul>
 * <p>
 * Invalid or missing parameters result in an {@link IllegalParamException}.
 */
public class FilterParamParser implements ParamParser {
    protected final FilterSet filters;
    protected final UrlDecoder decoder;
    protected final boolean decodePath;
    protected final boolean decodeQuery;

    /**
     * Creates a new parser.
     *
     * @param filters     the filter set used for type conversion and validation
     * @param decoder     the decoder used for URL decoding
     * @param decodePath  whether path parameters should be URL-decoded
     * @param decodeQuery whether query parameters should be URL-decoded
     */
    public FilterParamParser(FilterSet filters, UrlDecoder decoder, boolean decodePath, boolean decodeQuery) {
        this.filters = filters;
        this.decoder = decoder;
        this.decodePath = decodePath;
        this.decodeQuery = decodeQuery;
    }

    /**
     * Builds a detailed error message for an invalid parameter.
     *
     * @param type      the parameter type ("Path" or "Query")
     * @param parameter the parameter definition
     * @param value     the offending value
     * @param reason    the reason for invalidity
     * @return a formatted error message
     */
    protected String getIllegalParamMessage(String type, Parameter parameter, Object value, String reason) {
        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
    }

    private void processPathParam(PathParameter param, String raw, Map<String, Object> map) {
        var name = param.getName();
        var type = param.getType();
        if (type == null) {
            map.put(name, raw);
            return;
        }
        var filter = filters.get(type);
        if (filter == null) {
            map.put(name, raw);
            return;
        }
        var object = filter.process(raw);
        map.put(name, object);
    }

    private void processPathParamsNoDecode(HttpRequest request, PathParameter[] params) {
        var segments = request.pathSegments();
        var map = request.pathParams();
        PathParameter param = null;
        String raw = null;
        try {
            for (var i = 0; i < params.length; ++i) {
                param = params[i];
                raw = segments.get(param.getIndex());
                processPathParam(param, raw, map);
            }
        } catch (Throwable e) {
            throw new IllegalParamException(getIllegalParamMessage("Path", param, raw, e.getMessage()), e);
        }
    }

    private void processPathParamsWithDecode(HttpRequest request, PathParameter[] params) {
        var segments = request.pathSegments();
        var map = request.pathParams();
        PathParameter param = null;
        String raw = null;
        try {
            for (var i = 0; i < params.length; ++i) {
                param = params[i];
                raw = decoder.decode(segments.get(param.getIndex()));
                processPathParam(param, raw, map);
            }
        } catch (Throwable e) {
            throw new IllegalParamException(getIllegalParamMessage("Path", param, raw, e.getMessage()), e);
        }
    }

    /**
     * Processes path parameters for the given request.
     * <p>
     * Each segment is optionally decoded and passed through the configured {@link FilterSet}.
     *
     * @param request the HTTP request
     * @param params  the path parameter definitions
     * @throws IllegalParamException if decoding or filtering fails
     */
    protected void processPathParams(HttpRequest request, PathParameter[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        if (decodePath) {
            processPathParamsWithDecode(request, params);
        } else {
            processPathParamsNoDecode(request, params);
        }
    }

    /**
     * Decodes query parameter keys and values in place.
     *
     * @param queries the raw query map
     * @throws IllegalParamException if decoding fails
     */
    protected void decodeQueryParams(List<String> queries) {
        if (queries == null) {
            return;
        }
        var size = queries.size();
        for (var i = 0; i < size; ++i) {
            queries.set(i, decoder.decode(queries.get(i)));
        }
    }

    /**
     * Decodes query parameter keys and values in place.
     *
     * @param queries the raw query map
     * @throws IllegalParamException if decoding fails
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void decodeQueryParams(Map<String, List<Object>> queries) {
        try {
            var iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                decodeQueryParams((List) entry.getValue());
                var key = entry.getKey();
                var decoded = decoder.decode(key);
                if (decoded.equals(key)) {
                    continue;
                }
                iterator.remove();
                queries.put(decoded, entry.getValue());
            }
        } catch (Throwable e) {
            throw new IllegalParamException("Query string is invalid. Reason: " + e.getMessage(), e);
        }
    }

    private void processQueryParam(QueryParameter param, List<Object> raw) {
        if (raw == null) {
            if (param.isRequired() == Boolean.TRUE) {
                throw new IllegalParamException("Missing required query parameter " + param);
            }
            return;
        }
        var type = param.getType();
        if (type == null || raw.isEmpty()) {
            return;
        }
        var filter = filters.get(type);
        if (filter == null) {
            return;
        }
        var size = raw.size();
        for (var i = 0; i < size; ++i) {
            raw.set(i, filter.process((String) raw.get(i)));
        }
    }

    /**
     * Applies filters and validation rules to query parameters.
     *
     * @param queries the raw query parameters
     * @param params  the query parameter definitions
     * @throws IllegalParamException if validation or filtering fails
     */
    protected void processQueryParams(Map<String, List<Object>> queries, QueryParameter[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        QueryParameter param = null;
        List<Object> raw = null;
        try {
            for (var i = 0; i < params.length; ++i) {
                param = params[i];
                raw = queries.get(param.getName());
                processQueryParam(param, raw);
            }
        } catch (Throwable e) {
            throw new IllegalParamException(getIllegalParamMessage("Query", param, raw, e.getMessage()), e);
        }
    }

    /**
     * Processes path and query parameters for the given request according to the provided metadata.
     *
     * @param request the HTTP request
     * @param data    the path and query parameter metadata
     * @throws IllegalParamException if parameter processing fails
     */
    @Override
    public void process(HttpRequest request, PathData data) {
        var queries = request.queryParams();
        if (decodeQuery && !queries.isEmpty()) {
            decodeQueryParams(queries);
        }
        if (data != null) {
            processPathParams(request, data.getPathParams());
            processQueryParams(queries, data.getQueryParams());
        }
    }
}
