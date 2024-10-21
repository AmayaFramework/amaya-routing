package io.github.amayaframework.parser;

import io.github.amayaframework.path.QueryParameter;

/**
 * An interface describing an abstract query parameter declaration parser.
 */
public interface QueryParameterParser {

    /**
     * Parses given parameter declaration as {@link QueryParameter}.
     *
     * @param parameter the specified query parameter declaration
     * @return the {@link QueryParameter} instance
     */
    QueryParameter parse(String parameter);
}
