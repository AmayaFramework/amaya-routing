package io.github.amayaframework.parser;

import io.github.amayaframework.path.Path;

/**
 * An interface describing an abstract path template parser.
 */
public interface PathParser {

    /**
     * Parses given http path template as {@link Path}.
     *
     * @param template the specified path template
     * @return the {@link Path} instance
     */
    Path parse(String template);
}
