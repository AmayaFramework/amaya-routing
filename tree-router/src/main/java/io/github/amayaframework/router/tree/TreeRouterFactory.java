package io.github.amayaframework.router.tree;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.Router;
import io.github.amayaframework.router.RouterFactory;
import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link RouterFactory} that uses path segment tree for dynamic routing.
 */
public final class TreeRouterFactory implements RouterFactory {
    private final Tokenizer tokenizer;

    /**
     * Constructs a {@link TreeRouterFactory} instance with given {@link Tokenizer}.
     *
     * @param tokenizer the specified {@link Tokenizer} instance, must be non-null
     */
    public TreeRouterFactory(Tokenizer tokenizer) {
        this.tokenizer = Objects.requireNonNull(tokenizer);
    }

    /**
     * Constructs a {@link TreeRouterFactory} instance with {@link io.github.amayaframework.tokenize.PlainTokenizer}.
     */
    public TreeRouterFactory() {
        this.tokenizer = Tokenizers.PLAIN_TOKENIZER;
    }

    @Override
    public <T> Router<T> create(Map<Path, T> paths) {
        var statics = new HashMap<String, PathContext<T>>();
        var dynamics = new LinkedList<Path>();
        for (var entry : paths.entrySet()) {
            var path = entry.getKey();
            // Add all paths to state machine to prevent undefined behaviour
            dynamics.add(path);
            // If path not is dynamic, register it in fast static map
            if (!path.isDynamic()) {
                var context = new PathContext<>(path.getData(), entry.getValue());
                statics.put(path.getPath(), context);
            }
        }
        if (statics.size() == dynamics.size()) {
            return new TreeRouter<>(tokenizer, statics, null);
        }
        var root = new PathNode();
        for (var path : dynamics) {
            var context = new PathContext<T>(path.getData(), paths.get(path));
            root.attach(path, context);
        }
        return new TreeRouter<>(tokenizer, statics, root);
    }
}
