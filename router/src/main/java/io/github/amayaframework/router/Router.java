package io.github.amayaframework.router;

import java.util.function.Supplier;

/**
 * @param <T>
 */
public interface Router<T> {

    /**
     * @param path
     * @param supplier
     * @return
     */
    PathContext<T> process(String path, Supplier<Iterable<String>> supplier);

    /**
     * @param path
     * @return
     */
    PathContext<T> process(String path);
}
