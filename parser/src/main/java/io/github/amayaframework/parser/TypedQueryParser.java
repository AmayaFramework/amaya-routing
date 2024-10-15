package io.github.amayaframework.parser;

import io.github.amayaframework.path.QueryParameter;

public final class TypedQueryParser implements QueryParameterParser {
    private final char delim;

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
