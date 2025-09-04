package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.QueryParameter;

/**
 * Defines a parser for query parameter declarations in URI templates.
 * <p>
 * A {@link QueryParameterParser} converts a raw query declaration
 * (e.g. <code>page!:int</code>) into a {@link QueryParameter} instance,
 * attaching the parameter name, requirement flag, and optional type identifier.
 * </p>
 */
public interface QueryParameterParser {

    /**
     * Parses the given query parameter declaration into a {@link QueryParameter}.
     *
     * @param parameter the raw query parameter declaration string
     * @return a {@link QueryParameter} instance representing the declaration
     * @throws IllegalArgumentException if the declaration is invalid
     */
    QueryParameter parse(String parameter);
}
