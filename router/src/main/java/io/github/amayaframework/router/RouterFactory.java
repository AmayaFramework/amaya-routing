package io.github.amayaframework.router;

import java.util.Map;

/**
 *
 */
public interface RouterFactory {

    /**
     * @param paths
     * @param <T>
     * @return
     */
    <T> Router<T> create(Map<Path, T> paths);
}
