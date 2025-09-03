package io.github.amayaframework.routing;

import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.path.Parameter;
import io.github.amayaframework.path.PathData;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;

import java.util.List;
import java.util.Map;

public class FilterParamParser implements ParamParser {
    protected final FilterSet filters;
    protected final UrlDecoder decoder;
    protected final boolean decodePath;
    protected final boolean decodeQuery;

    public FilterParamParser(FilterSet filters, UrlDecoder decoder, boolean decodePath, boolean decodeQuery) {
        this.filters = filters;
        this.decoder = decoder;
        this.decodePath = decodePath;
        this.decodeQuery = decodeQuery;
    }

    protected String getBadRequestMessage(String type, Parameter parameter, Object value, String reason) {
        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
    }

    protected String processPathParams(HttpRequest request, List<PathParameter> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        var segments = request.pathSegments();
        var map = request.pathParams();
        for (var param : params) {
            var raw = segments.get(param.getIndex());
            if (decodePath) {
                try {
                    raw = decoder.decode(raw);
                } catch (Throwable e) {
                    return getBadRequestMessage("Path", param, raw, e.getMessage());
                }
            }
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

    protected void decodeQueryParams(List<String> queries) {
        if (queries == null) {
            return;
        }
        var size = queries.size();
        for (var i = 0; i < size; ++i) {
            queries.set(i, decoder.decode(queries.get(i)));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected String decodeQueryParams(Map<String, List<Object>> queries) {
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
            return null;
        } catch (Throwable e) {
            return "Query string is invalid. Reason: " + e.getMessage();
        }
    }

    protected String processQueryParams(HttpRequest request, List<QueryParameter> parameters) {
        var queries = request.queryParams();
        if (queries == null || queries.isEmpty()) {
            return null;
        }
        String message;
        if (decodeQuery && (message = decodeQueryParams(queries)) != null) {
            return message;
        }
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
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

    @Override
    public String process(HttpRequest request, PathData data) {
        var ret = processPathParams(request, data.getPathParams());
        if (ret != null) {
            return ret;
        }
        return processQueryParams(request, data.getQueryParams());
    }
}
