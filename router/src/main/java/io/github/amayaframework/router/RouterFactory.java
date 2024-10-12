package io.github.amayaframework.router;

import java.util.Map;

/**
 * An interface describing an abstract {@link Router} factory.
 */
public interface RouterFactory {

    /**
     * Creates router with given path map.
     *
     * @param paths the specified path map
     * @param <T>   the path context value type
     * @return the {@link Router} instance
     */
    <T> Router<T> create(Map<Path, T> paths);
}
