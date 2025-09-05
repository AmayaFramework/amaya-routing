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
 * TODO
 */
public class FilterParamParser implements ParamParser {
    protected final FilterSet filters;
    protected final UrlDecoder decoder;
    protected final boolean decodePath;
    protected final boolean decodeQuery;

    /**
     * TODO
     * @param filters
     * @param decoder
     * @param decodePath
     * @param decodeQuery
     */
    public FilterParamParser(FilterSet filters, UrlDecoder decoder, boolean decodePath, boolean decodeQuery) {
        this.filters = filters;
        this.decoder = decoder;
        this.decodePath = decodePath;
        this.decodeQuery = decodeQuery;
    }

    /**
     * TODO
     * @param type
     * @param parameter
     * @param value
     * @param reason
     * @return
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
     * TODO
     * @param request
     * @param params
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
     * TODO
     * @param queries
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
     * TODO
     * @param queries
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
     * TODO
     * @param queries
     * @param params
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
