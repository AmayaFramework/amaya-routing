package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link RouterFactory} that uses hash mapping for static routing.
 */
public final class StaticRouterFactory implements RouterFactory {
    private final Tokenizer tokenizer;

    /**
     * Constructs a {@link StaticRouterFactory} instance with {@link Tokenizer}.
     *
     * @param tokenizer the specified {@link Tokenizer} instance, must be non-null
     */
    public StaticRouterFactory(Tokenizer tokenizer) {
        this.tokenizer = Objects.requireNonNull(tokenizer);
    }

    /**
     * Constructs a {@link StaticRouterFactory} instance with {@link io.github.amayaframework.tokenize.PlainTokenizer}.
     */
    public StaticRouterFactory() {
        this.tokenizer = Tokenizers.PLAIN_TOKENIZER;
    }

    @Override
    public <T> Router<T> create(Map<Path, T> paths) {
        var statics = new HashMap<String, PathContext<T>>();
        for (var entry : paths.entrySet()) {
            var path = entry.getKey();
            if (path.dynamic) {
                throw new IllegalArgumentException("Static router does not support dynamic paths");
            }
            var context = new PathContext<>(path.data, entry.getValue());
            statics.put(path.path, context);
        }
        return new StaticRouter<>(tokenizer, statics);
    }
}
