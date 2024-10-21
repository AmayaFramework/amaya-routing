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

/**
 * An abstract class that implements the PathParser interface, providing common functionality
 * for parsing paths and query strings. This class serves as a base for specific path parser
 * implementations, offering methods to handle path parameters and query parameters.
 */
public abstract class AbstractPathParser implements PathParser {

    /**
     * The delimiter used to separate path segments.
     */
    protected static final String PATH_DELIM = "/";

    /**
     * The delimiter used to separate query parameters.
     */
    protected static final String QUERY_DELIM = "&";

    /**
     * The character that indicates the beginning of the query string in a URL.
     */
    protected static final char QUERY_STRING_DELIM = '?';

    /**
     * The tokenizer used for splitting strings into tokens based on defined delimiters.
     */
    protected final Tokenizer tokenizer;

    /**
     * A string representing common dynamic segment in the path.
     */
    protected final String any;

    /**
     * A parser responsible for handling path parameters.
     */
    protected final PathParameterParser pathParser;

    /**
     * A parser responsible for handling query parameters.
     */
    protected final QueryParameterParser queryParser;

    /**
     * Constructs an instance of AbstractPathParser with the specified tokenizer,
     * dynamic segment representation, and parameter parsers.
     *
     * @param tokenizer   the tokenizer to be used for parsing paths and queries
     * @param any         a string representation of common dynamic segment
     * @param pathParser  the parser responsible for parsing path parameters
     * @param queryParser the parser responsible for parsing query parameters
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
     * Unwraps the specified path parameter, converting it into a usable format.
     *
     * @param parameter the path parameter to unwrap
     * @return the unwrapped path parameter as a String
     */
    protected abstract String unwrapPathParameter(String parameter);

    /**
     * Parses a given path string into a Path object, extracting segments and parameters.
     *
     * @param path the path string to parse
     * @return a {@link Path} object representing the parsed path, including segments and parameters
     */
    protected Path parsePathString(String path) {
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
        data.setPathParameters(parameters);
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
        if (query.isEmpty() || query.isBlank()) {
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
        data.setQueryParameters(query);
        return path;
    }
}
