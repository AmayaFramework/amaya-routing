package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.PathParameter;

/**
 * Implementation of {@link PathParameterParser} that supports typed parameters.
 * <p>
 * Format:
 * <pre>{@code
 *   name:type
 * }</pre>
 * where <code>type</code> is optional.
 * </p>
 */
public final class TypedPathParameterParser implements PathParameterParser {
    private final char delim;

    /**
     * Creates a parser with the given type delimiter.
     *
     * @param delim the delimiter between name and type (e.g. ':')
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
