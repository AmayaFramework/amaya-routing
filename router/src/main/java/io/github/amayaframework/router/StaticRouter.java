package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link Router} implementation optimized for static paths.
 *
 * @param <T> the type of the context value
 */
public final class StaticRouter<T> extends AbstractRouter<T> {
    private final Map<String, PathContext<T>> statics;

    /**
     * Constructs a new {@code StaticRouter} with the given tokenizer and static path mappings.
     * <p>
     * This router is optimized for exact (static) path lookups without dynamic segments.
     *
     * @param tokenizer the tokenizer used for path normalization
     * @param statics   the map of normalized static paths to their {@link PathContext} values
     */
    public StaticRouter(Tokenizer tokenizer, Map<String, PathContext<T>> statics) {
        super(tokenizer);
        this.statics = statics;
    }

    @Override
    public PathContext<T> process(String path, Supplier<Iterable<String>> supplier) {
        return statics.get(PathUtil.normalize(path));
    }
}
