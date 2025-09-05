package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.PathData;
import io.github.amayaframework.path.PathParameter;
import io.github.amayaframework.path.QueryParameter;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Base class for {@link PathParser} implementations.
 * <p>
 * Provides common functionality for parsing path and query segments,
 * detecting {@link PathParameter} and {@link QueryParameter} declarations,
 * and normalizing template strings.
 * </p>
 */
public abstract class AbstractPathParser implements PathParser {

    /**
     * Delimiter between path segments.
     */
    protected static final String PATH_DELIM = "/";

    /**
     * Delimiter between query parameters.
     */
    protected static final String QUERY_DELIM = "&";

    /**
     * Character indicating the start of a query string.
     */
    protected static final char QUERY_STRING_DELIM = '?';

    /**
     * Tokenizer for splitting path and query strings.
     */
    protected final Tokenizer tokenizer;

    /**
     * Placeholder for dynamic segments in normalized paths.
     */
    protected final String any;

    /**
     * Parser for path parameters.
     */
    protected final PathParameterParser pathParser;

    /**
     * Parser for query parameters.
     */
    protected final QueryParameterParser queryParser;

    /**
     * Constructs an {@link AbstractPathParser}.
     *
     * @param tokenizer   tokenizer for splitting path and query strings
     * @param any         placeholder symbol for dynamic path segments
     * @param pathParser  parser for path parameter declarations
     * @param queryParser parser for query parameter declarations
     */
    protected AbstractPathParser(Tokenizer tokenizer,
                                 String any,
                                 PathParameterParser pathParser,
                                 QueryParameterParser queryParser) {
        this.tokenizer = tokenizer;
        this.any = any;
        this.pathParser = pathParser;
        this.queryParser = queryParser;
    }

    /**
     * Extracts the parameter declaration from a segment if present.
     *
     * @param parameter the raw path segment
     * @return the unwrapped declaration string, or {@code null} if not a parameter
     */
    protected abstract String unwrapPathParameter(String parameter);

    /**
     * Parses a given path string into a Path object, extracting segments and parameters.
     *
     * @param path the path string to parse
     * @return a {@link Path} object representing the parsed path, including segments and parameters
     */
    @SuppressWarnings("unchecked")
    protected Path parsePathString(String path) {
        if (path.isEmpty() || path.equals("/")) {
            return new Path("/", Collections.EMPTY_LIST, false);
        }
        var parameters = new ArrayList<PathParameter>();
        var set = new HashSet<String>();
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
            var name = parameter.getName();
            if (set.contains(name)) {
                throw new IllegalArgumentException("Duplicate path parameter found: " + parameter);
            }
            set.add(name);
            dynamic = true;
            normalized.append('/').append(any);
            segments.add(null);
            parameters.add(parameter);
            index++;
        }
        var ret = new Path(normalized.toString(), segments, dynamic);
        if (parameters.isEmpty()) {
            return ret;
        }
        var data = new PathData();
        data.setPathParams(parameters.toArray(new PathParameter[0]));
        ret.setData(data);
        return ret;
    }

    /**
     * Parses a given query string into a list of {@link QueryParameter} objects.
     *
     * @param query the query string to parse
     * @return a list of QueryParameter objects extracted from the query string,
     * or null if the query string is empty or blank
     */
    protected List<QueryParameter> parseQueryString(String query) {
        if (query.isBlank()) {
            return null;
        }
        var tokens = tokenizer.tokenize(query, QUERY_DELIM);
        var parameters = new ArrayList<QueryParameter>();
        var set = new HashSet<String>();
        for (var token : tokens) {
            var parameter = queryParser.parse(token);
            var name = parameter.getName();
            if (set.contains(name)) {
                throw new IllegalArgumentException("Duplicate query parameter found: " + parameter);
            }
            set.add(name);
            parameters.add(parameter);
        }
        return parameters;
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
        data.setQueryParams(query.toArray(new QueryParameter[0]));
        return path;
    }
}
