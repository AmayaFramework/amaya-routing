package io.github.amayaframework.router.tree;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.router.*;
import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link RouterFactory} that uses a path segment tree
 * for dynamic routing.
 *
 * <p>When creating a router:</p>
 * <ul>
 *   <li>All paths are added to the tree to prevent undefined behavior.</li>
 *   <li>Static (non-dynamic) paths are also stored in a fast lookup map
 *       for constant-time resolution.</li>
 *   <li>If all routes are static, a {@link StaticRouter} is returned
 *       instead of a tree-based router.</li>
 * </ul>
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
    @SuppressWarnings("unchecked")
    public <T> Router<T> create(Map<Path, T> paths) {
        if (paths == null || paths.isEmpty()) {
            return EmptyRouter.INSTANCE;
        }
        var statics = new HashMap<String, PathContext<T>>();
        var dynamics = new LinkedList<Path>();
        for (var entry : paths.entrySet()) {
            var path = entry.getKey();
            // Add all paths to state machine to prevent undefined behavior
            dynamics.add(path);
            // If the path is not dynamic, register it in a fast static map
            if (!path.isDynamic()) {
                statics.put(path.getPath(), new PathContext<>(path.getData(), entry.getValue()));
            }
        }
        if (statics.size() == dynamics.size()) {
            return new StaticRouter<>(tokenizer, statics);
        }
        var root = new PathNode();
        for (var path : dynamics) {
            root.attach(path, new PathContext<T>(path.getData(), paths.get(path)));
        }
        return new TreeRouter<>(tokenizer, statics, root);
    }
}
