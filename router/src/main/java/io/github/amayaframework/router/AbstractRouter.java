package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizer;

public abstract class AbstractRouter<T> implements Router<T> {
    protected final Tokenizer tokenizer;

    protected AbstractRouter(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public PathContext<T> process(String path) {
        return process(path, () -> tokenizer.tokenize(path, "/"));
    }
}
