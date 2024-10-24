package io.github.amayaframework.parser;

import io.github.amayaframework.path.PathParameter;

/**
 * An implementation of {@link PathParameterParser} that works with the following path parameter template format:
 * &lt;name&gt;:&lt;type&gt;. Type segment is optional and can be omitted.
 */
public final class TypedPathParameterParser implements PathParameterParser {
    private final char delim;

    /**
     * Constructs a {@link TypedPathParameterParser} instance with given type delimiter.
     *
     * @param delim the specified type delimiter
     */
    public TypedPathParameterParser(char delim) {
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
