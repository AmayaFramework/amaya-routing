package io.github.amayaframework.router;

import java.util.function.Supplier;

/**
 * Defines a generic router that maps normalized paths to
 * {@link PathContext} instances.
 *
 * @param <T> the type of the context value associated with each path
 */
public interface Router<T> {

    /**
     * Resolves the given path into a {@link PathContext}.
     *
     * @param path     the raw path string to resolve
     * @param supplier supplier of tokenized path segments
     * @return the matching {@link PathContext}, or {@code null} if not found
     */
    PathContext<T> process(String path, Supplier<Iterable<String>> supplier);

    /**
     * Resolves the given path into a {@link PathContext}.
     * <p>
     * This overload tokenizes the path automatically using the router’s
     * configured {@link io.github.amayaframework.tokenize.Tokenizer}.
     * </p>
     *
     * @param path the raw path string to resolve
     * @return the matching {@link PathContext}, or {@code null} if not found
     */
    PathContext<T> process(String path);
}
