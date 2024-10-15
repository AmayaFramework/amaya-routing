package io.github.amayaframework.parser;

import io.github.amayaframework.path.PathParameter;

public final class TypedPathParser implements PathParameterParser {
    private final char delim;

    public TypedPathParser(char delim) {
        this.delim = delim;
    }

    @Override
    public PathParameter parse(String parameter, int index) {
        parameter = parameter.strip();
        if (parameter.isEmpty()) {
            throw new IllegalArgumentException("Empty path parameter");
        }
        var position = parameter.indexOf(delim);
        if (position < 0) {
            return new PathParameter(parameter, index, null);
        }
        var name = parameter.substring(0, position).stripTrailing();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty path parameter name");
        }
        var type = parameter.substring(position + 1).stripLeading();
        return new PathParameter(name, index, type.isEmpty() ? null : type);
    }
}
