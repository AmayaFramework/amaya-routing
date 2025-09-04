package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;

/**
 * Abstract base class for {@link Router} implementations.
 *
 * @param <T> the type of the context value
 */
public abstract class AbstractRouter<T> implements Router<T> {

    /**
     * Default tokenizer for path segmentation.
     */
    protected final Tokenizer tokenizer;

    /**
     * Creates an {@link AbstractRouter} with the given {@link Tokenizer}.
     *
     * @param tokenizer tokenizer to split incoming paths, must be non-null
     */
    protected AbstractRouter(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public PathContext<T> process(String path) {
        return process(path, () -> tokenizer.tokenize(path, "/"));
    }
}
