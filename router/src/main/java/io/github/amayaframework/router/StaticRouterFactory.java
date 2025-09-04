package io.github.amayaframework.router;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link RouterFactory} that creates {@link Router} instances
 * for static path mappings.
 * <p>
 * Dynamic paths are not supported; attempting to register one
 * will result in {@link IllegalArgumentException}.
 * </p>
 */
public final class StaticRouterFactory implements RouterFactory {
    private final Tokenizer tokenizer;

    /**
     * Creates a factory using the given {@link Tokenizer}.
     *
     * @param tokenizer tokenizer for path processing
     */
    public StaticRouterFactory(Tokenizer tokenizer) {
        this.tokenizer = Objects.requireNonNull(tokenizer);
    }

    /**
     * Creates a factory using {@link Tokenizers#PLAIN_TOKENIZER}.
     */
    public StaticRouterFactory() {
        this.tokenizer = Tokenizers.PLAIN_TOKENIZER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Router<T> create(Map<Path, T> paths) {
        if (paths == null || paths.isEmpty()) {
            return EmptyRouter.INSTANCE;
        }
        var statics = new HashMap<String, PathContext<T>>();
        for (var entry : paths.entrySet()) {
            var path = entry.getKey();
            if (path.isDynamic()) {
                throw new IllegalArgumentException("Static router does not support dynamic paths");
            }
            statics.put(path.getPath(), new PathContext<>(path.getData(), entry.getValue()));
        }
        return new StaticRouter<>(tokenizer, statics);
    }
}
