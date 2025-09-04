package io.github.amayaframework.router;

import io.github.amayaframework.path.Path;

import java.util.Map;

/**
 * Factory for creating {@link Router} instances from a mapping of
 * {@link Path} definitions to context values.
 */
public interface RouterFactory {

    /**
     * Creates a new {@link Router} instance for the given path map.
     *
     * @param paths the map of {@link Path} definitions to associated values
     * @param <T>   the type of the context values
     * @return a new {@link Router} instance
     */
    <T> Router<T> create(Map<Path, T> paths);
}
