package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.PathParameter;

/**
 * Defines a parser for path parameter declarations in URI templates.
 * <p>
 * A {@link PathParameterParser} converts a raw parameter declaration
 * (e.g. <code>id:int</code>) into a {@link PathParameter} instance,
 * attaching the parameter name, type identifier, and its position in the path.
 * </p>
 */
public interface PathParameterParser {

    /**
     * Parses the given parameter declaration into a {@link PathParameter}.
     *
     * @param parameter the raw parameter declaration string
     * @param index     the position of the parameter in the URI path
     * @return a {@link PathParameter} instance representing the declaration
     * @throws IllegalArgumentException if the declaration is invalid
     */
    PathParameter parse(String parameter, int index);
}
