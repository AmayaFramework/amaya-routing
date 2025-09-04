package io.github.amayaframework.router.tree;

import io.github.amayaframework.router.AbstractRouter;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.PathUtil;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Router implementation that uses a segment tree for dynamic path resolution.
 *
 * <p>This router first attempts to resolve paths via a fast static map
 * (exact string matches). If no match is found, it falls back to
 * the {@link PathNode} tree for dynamic resolution.</p>
 *
 * @param <T> the type of values stored in {@link PathContext}
 */
final class TreeRouter<T> extends AbstractRouter<T> {
    private final Map<String, PathContext<T>> statics;
    private final PathNode root;

    TreeRouter(Tokenizer tokenizer, Map<String, PathContext<T>> statics, PathNode root) {
        super(tokenizer);
        this.statics = statics;
        this.root = root;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PathContext<T> process(String path, Supplier<Iterable<String>> supplier) {
        var found = statics.get(PathUtil.normalize(path));
        if (found != null) {
            return found;
        }
        return root.lookup(supplier.get());
    }
}
