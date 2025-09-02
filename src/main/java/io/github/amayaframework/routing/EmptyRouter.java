package io.github.amayaframework.routing;

import io.github.amayaframework.router.PathContext;

import java.util.function.Supplier;

public final class EmptyRouter implements HttpRouter {
    public static final HttpRouter INSTANCE = new EmptyRouter();

    public static HttpRouter emptyRouter() {
        return INSTANCE;
    }

    private EmptyRouter() {
    }

    @Override
    public PathContext<MethodMap> process(String path, Supplier<Iterable<String>> supplier) {
        return null;
    }

    @Override
    public PathContext<MethodMap> process(String path) {
        return null;
    }

    @Override
    public boolean empty() {
        return true;
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean isUni() {
        return true;
    }
}
