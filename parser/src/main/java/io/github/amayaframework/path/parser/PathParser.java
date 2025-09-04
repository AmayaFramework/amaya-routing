package io.github.amayaframework.path.parser;

import io.github.amayaframework.path.Path;

/**
 * Defines a parser for URI path templates.
 * <p>
 * A {@link PathParser} processes an entire path template string
 * (including optional query definitions) into a {@link Path} descriptor.
 * </p>
 * <p>
 * Example:
 * <pre>{@code
 *   /users/{id:int}?active!:boolean
 * }</pre>
 * </p>
 */
public interface PathParser {

    /**
     * Parses the given path template string.
     *
     * @param template the raw path template
     * @return a {@link Path} representing the parsed template
     * @throws IllegalArgumentException if the template is invalid
     */
    Path parse(String template);
}
