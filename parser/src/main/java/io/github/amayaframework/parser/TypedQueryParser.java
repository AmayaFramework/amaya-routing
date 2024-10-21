package io.github.amayaframework.parser;

import io.github.amayaframework.path.QueryParameter;

/**
 * An implementation of {@link QueryParameterParser} that works with the following path parameter template format:
 * &lt;name&gt;&lt;?!&gt;:&lt;type&gt;. Requirement and type segments is optional and can be omitted.
 * '?' means optional query requirement, '!' means strict requirement.
 * If this segment is omitted, parameter requirement is null.
 */
public final class TypedQueryParser implements QueryParameterParser {
    private final char delim;

    /**
     * Constructs a {@link TypedQueryParser} instance with given type delimiter.
     *
     * @param delim the specified type delimiter
     */
    public TypedQueryParser(char delim) {
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
