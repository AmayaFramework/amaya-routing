package io.github.amayaframework.mapper;

import java.util.List;
import java.util.Map;

/**
 * An interface describing an abstract {@link PathMapper} factory.
 */
public interface PathMapperFactory {

    /**
     * Creates a {@link PathMapper} instance with given path patterns.
     *
     * @param paths the {@link Map} containing pattern:segment pairs, must be non-null
     * @return the {@link PathMapper} instance
     */
    PathMapper create(Map<String, List<String>> paths);

    /**
     * Creates a {@link PathMapper} instance with given path patterns.
     *
     * @param paths the {@link Iterable} containing path patterns, must be non-null
     * @return the {@link PathMapper} instance
     */
    PathMapper create(Iterable<String> paths);
}
