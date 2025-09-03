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

    protected String getIllegalParamMessage(String type, Parameter parameter, Object value, String reason) {
        return type + " parameter " + parameter + " with value '" + value + "' is invalid. Reason: " + reason;
    }

    protected void processPathParams(HttpRequest request, List<PathParameter> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        var segments = request.pathSegments();
        var map = request.pathParams();
        for (var param : params) {
            var raw = segments.get(param.getIndex());
            if (decodePath) {
                try {
                    raw = decoder.decode(raw);
                } catch (Throwable e) {
                    throw new IllegalParamException(getIllegalParamMessage("Path", param, raw, e.getMessage()), e);
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
                throw new IllegalParamException(getIllegalParamMessage("Path", param, raw, e.getMessage()), e);
            }
        }
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

    protected void processQueryParams(HttpRequest request, List<QueryParameter> parameters) {
        var queries = request.queryParams();
        if (queries == null || queries.isEmpty()) {
            return;
        }
        if (decodeQuery) {
            decodeQueryParams(queries);
        }
        if (parameters == null || parameters.isEmpty()) {
            return;
        }
        for (var parameter : parameters) {
            var raw = queries.get(parameter.getName());
            if (raw == null) {
                if (parameter.isRequired() == Boolean.TRUE) {
                    throw new IllegalParamException("Missing required query parameter " + parameter);
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
                throw new IllegalParamException(getIllegalParamMessage("Query", parameter, raw, e.getMessage()), e);
            }
        }
    }

    @Override
    public void process(HttpRequest request, PathData data) {
        processPathParams(request, data.getPathParams());
        processQueryParams(request, data.getQueryParams());
    }
}
