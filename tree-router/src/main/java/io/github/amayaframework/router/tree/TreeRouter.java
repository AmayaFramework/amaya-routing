package io.github.amayaframework.router.tree;

import io.github.amayaframework.router.AbstractRouter;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.PathUtil;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

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
        if (root == null) {
            return null;
        }
        return root.lookup(supplier.get());
    }
}
