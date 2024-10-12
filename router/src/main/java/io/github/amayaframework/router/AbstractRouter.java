package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;

/**
 * The skeletal implementation of {@link Router}.
 *
 * @param <T> the path context type
 */
public abstract class AbstractRouter<T> implements Router<T> {
    /**
     * The tokenizer to be used by default.
     */
    protected final Tokenizer tokenizer;

    /**
     * Constructs an {@link AbstractRouter} instance with given {@link Tokenizer}.
     *
     * @param tokenizer the specified {@link Tokenizer} instance, must be non-null
     */
    protected AbstractRouter(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public PathContext<T> process(String path) {
        return process(path, () -> tokenizer.tokenize(path, "/"));
    }
}
