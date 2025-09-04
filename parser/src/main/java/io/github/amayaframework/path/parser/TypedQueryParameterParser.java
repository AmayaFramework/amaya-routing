package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.QueryParameter;

/**
 * Implementation of {@link QueryParameterParser} that supports typed
 * and required/optional query parameters.
 * <p>
 * Format:
 * <pre>{@code
 *   name!:type
 *   name?:type
 *   name:type
 *   name
 * }</pre>
 * <ul>
 *   <li><code>!</code> = required</li>
 *   <li><code>?</code> = optional</li>
 *   <li>no flag = unspecified</li>
 *   <li><code>type</code> = optional type identifier</li>
 * </ul>
 */
public final class TypedQueryParameterParser implements QueryParameterParser {
    private final char delim;

    /**
     * Creates a parser with the given type delimiter.
     *
     * @param delim the delimiter between name and type (e.g. ':')
     */
    public TypedQueryParameterParser(char delim) {
        this.delim = delim;
    }

    private static void checkEmpty(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty query parameter name");
        }
    }

    private static QueryParameter parse(String name, String type) {
        var end = name.length() - 1;
        var last = name.charAt(end);
        if (last == '?') {
            var value = name.substring(0, end);
            checkEmpty(value);
            return new QueryParameter(value, false, type);
        }
        if (last == '!') {
            var value = name.substring(0, end);
            checkEmpty(value);
            return new QueryParameter(value, true, type);
        }
        return new QueryParameter(name, null, type);
    }

    @Override
    public QueryParameter parse(String parameter) {
        parameter = parameter.strip();
        if (parameter.isEmpty()) {
            throw new IllegalArgumentException("Illegal query parameter: " + parameter);
        }
        var index = parameter.indexOf(delim);
        if (index < 0) {
            return parse(parameter, null);
        }
        var name = parameter.substring(0, index).stripTrailing();
        checkEmpty(name);
        var type = parameter.substring(index + 1).stripLeading();
        return parse(name, type.isEmpty() ? null : type);
    }
}
