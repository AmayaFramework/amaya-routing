package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

import java.util.concurrent.CompletableFuture;

/**
 * A mixed-mode routing task implementation.
 * <p>
 * This class extends {@link AbstractRoutingTask} and implements {@link TaskConsumer},
 * supporting both synchronous and asynchronous execution modes.
 * <p>
 * Unlike {@link UniRoutingTask}, it adapts its behavior depending on the type
 * of the resolved handler:
 * <ul>
 *   <li>If the handler is asynchronous or unified, it runs asynchronously via {@link Task#runAsync(Object)}.</li>
 *   <li>If the handler is purely synchronous, it runs in the current thread and returns a completed {@link CompletableFuture}.</li>
 * </ul>
 * <p>
 * It integrates with a {@link Router} to resolve routes and dispatch requests,
 * while also handling standard HTTP semantics such as 404 (Not Found),
 * 405 (Method Not Allowed), and automatic {@code OPTIONS} responses.
 */
public class MixedRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    /**
     * Creates a new mixed routing task.
     *
     * @param router        the underlying router used to resolve paths
     * @param parser        the parameter parser to apply (may be {@code null})
     * @param handleOptions whether {@code OPTIONS} requests should be handled automatically
     * @param cacheControl  optional value for the {@code Cache-Control} response header (may be {@code null})
     */
    public MixedRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    /**
     * Creates a new mixed routing task without a custom {@code Cache-Control} header.
     *
     * @param router        the underlying router used to resolve paths
     * @param parser        the parameter parser to apply (may be {@code null})
     * @param handleOptions whether {@code OPTIONS} requests should be handled automatically
     */
    public MixedRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    /**
     * Creates a new mixed routing task with default options:
     * <ul>
     *     <li>{@code handleOptions} is set to {@code true}</li>
     *     <li>{@code cacheControl} is {@code null}</li>
     * </ul>
     *
     * @param router the underlying router used to resolve paths
     * @param parser the parameter parser to apply (may be {@code null})
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
