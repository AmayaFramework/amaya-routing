package io.github.amayaframework.parser;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.PathData;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractPathParser implements PathParser {
    protected static final String PATH_DELIM = "/";
    protected static final String QUERY_DELIM = "&";
    protected static final char QUERY_STRING_DELIM = '?';
    protected final Tokenizer tokenizer;
    protected final String any;
    protected final PathParameterParser pathParser;
    protected final QueryParameterParser queryParser;

    protected AbstractPathParser(Tokenizer tokenizer,
                                 String any,
                                 PathParameterParser pathParser,
                                 QueryParameterParser queryParser) {
        this.tokenizer = tokenizer;
        this.any = any;
        this.pathParser = pathParser;
        this.queryParser = queryParser;
    }

    protected abstract String unwrapPathParameter(String parameter);

    protected Path parsePathString(String path) {
        var parameters = new HashSet<PathParameter>();
        var segments = new ArrayList<String>();
        var normalized = new StringBuilder();
        var dynamic = false;
        var tokens = tokenizer.tokenize(path, PATH_DELIM);
        var index = 0;
        for (var token : tokens) {
            // Handle generic segments
            if (token.equals(any)) {
                dynamic = true;
                normalized.append('/').append(any);
                segments.add(null);
                index++;
                continue;
            }
            var unwrapped = unwrapPathParameter(token);
            // Handle static segments
            if (unwrapped == null) {
                normalized.append('/').append(token);
                segments.add(token);
                index++;
                continue;
            }
            // Handle param definition segment
            var parameter = pathParser.parse(unwrapped, index);
            dynamic = true;
            normalized.append('/').append(any);
            segments.add(null);
            if (parameters.contains(parameter)) {
                throw new IllegalArgumentException("Duplicate path parameter found: " + parameter);
            }
            parameters.add(parameter);
            index++;
        }
        var ret = new Path(normalized.toString(), segments, dynamic);
        if (parameters.isEmpty()) {
            return ret;
        }
        var data = new PathData();
        data.setPathParameters(new ArrayList<>(parameters));
        ret.setData(data);
        return ret;
    }

    protected List<QueryParameter> parseQueryString(String query) {
        if (query.isEmpty() || query.isBlank()) {
            return null;
        }
        var tokens = tokenizer.tokenize(query, QUERY_DELIM);
        var parameters = new HashSet<QueryParameter>();
        for (var token : tokens) {
            var parameter = queryParser.parse(token);
            if (parameters.contains(parameter)) {
                throw new IllegalArgumentException("Duplicate query parameter found: " + parameter);
            }
            parameters.add(parameter);
        }
        return new ArrayList<>(parameters);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Path parse(String template) {
        template = template.strip();
        if (template.isEmpty() || template.equals("/")) {
            return new Path("/", Collections.EMPTY_LIST, false);
        }
        var index = template.indexOf(QUERY_STRING_DELIM);
        if (index < 0) {
            return parsePathString(template);
        }
        var pathString = template.substring(0, index).stripTrailing();
        var queryString = template.substring(index + 1).stripLeading();
        var path = parsePathString(pathString);
        var query = parseQueryString(queryString);
        if (query == null || query.isEmpty()) {
            return path;
        }
        var data = path.getData();
        if (data == null) {
            data = new PathData();
            path.setData(data);
        }
        data.setQueryParameters(query);
        return path;
    }
}
