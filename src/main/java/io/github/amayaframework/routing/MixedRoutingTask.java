package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class MixedRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     * @param cacheControl
     */
    public MixedRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     */
    public MixedRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    /**
     *
     * @param router
     * @param parser
     */
    public MixedRoutingTask(Router<MethodMap> router, ParamParser parser) {
        super(router, parser, true, null);
    }

    @Override
    protected CompletableFuture<Void> runHandlerAsync(Task<HttpContext> handler, HttpContext context) {
        if (handler.isAsync() || handler.isUni()) {
            return handler.runAsync(context);
        }
        try {
            handler.run(context);
        } catch (Throwable e) {
            return CompletableFuture.failedFuture(e);
        }
        return CompletableFuture.completedFuture(null);
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
