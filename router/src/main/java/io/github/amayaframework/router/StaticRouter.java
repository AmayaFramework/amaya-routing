package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

final class StaticRouter<T> extends AbstractRouter<T> {
    private final Map<String, PathContext<T>> statics;

    StaticRouter(Tokenizer tokenizer, Map<String, PathContext<T>> statics) {
        super(tokenizer);
        this.statics = statics;
    }

    @Override
    public PathContext<T> process(String path, Supplier<Iterable<String>> supplier) {
        return statics.get(PathUtil.normalize(path));
    }
}
