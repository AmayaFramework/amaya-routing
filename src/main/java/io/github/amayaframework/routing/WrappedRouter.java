package io.github.amayaframework.routing;

import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.Router;

import java.util.function.Supplier;

final class WrappedRouter implements HttpRouter {
    final Router<MethodMap> router;
    final boolean sync;
    final boolean async;

    WrappedRouter(Router<MethodMap> router, boolean sync, boolean async) {
        this.router = router;
        this.sync = sync;
        this.async = async;
    }

    @Override
    public PathContext<MethodMap> process(String path, Supplier<Iterable<String>> supplier) {
        return router.process(path, supplier);
    }

    @Override
    public PathContext<MethodMap> process(String path) {
        return router.process(path);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isSync() {
        return sync;
    }

    @Override
    public boolean isAsync() {
        return async;
    }

    @Override
    public boolean isUni() {
        return sync && async;
    }
}
