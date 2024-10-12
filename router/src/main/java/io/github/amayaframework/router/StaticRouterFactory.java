package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class StaticRouterFactory implements RouterFactory {
    private final Tokenizer tokenizer;

    /**
     * @param tokenizer
     */
    public StaticRouterFactory(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     *
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
