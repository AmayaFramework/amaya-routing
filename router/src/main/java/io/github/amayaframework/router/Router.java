package io.github.amayaframework.router;

import java.util.function.Supplier;

/**
 * An interface describing an abstract router.
 *
 * @param <T> the path context value type
 */
public interface Router<T> {

    /**
     * Maps given path with stored contexts.
     *
     * @param path     the specified path to be mapped
     * @param supplier the specified path tokens supplier
     * @return the {@link PathContext} associated with path or null
     */
    PathContext<T> process(String path, Supplier<Iterable<String>> supplier);

    /**
     * Maps given path with stored contexts.
     *
     * @param path the specified path to be mapped
     * @return the {@link PathContext} associated with path or null
     */
    PathContext<T> process(String path);
}
