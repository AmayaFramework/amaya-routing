package io.github.amayaframework.parser;

import io.github.amayaframework.path.PathParameter;

/**
 * An interface describing an abstract path parameter declaration parser.
 */
public interface PathParameterParser {

    /**
     * Parses given parameter declaration as {@link PathParameter}.
     *
     * @param parameter the specified path parameter declaration
     * @param index     the index of path parameter in http uri
     * @return the {@link PathParameter} instance
     */
    PathParameter parse(String parameter, int index);
}
