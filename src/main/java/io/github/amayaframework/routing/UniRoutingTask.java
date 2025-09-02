package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import com.github.romanqed.jfunc.Exceptions;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.router.Router;

import java.util.concurrent.CompletableFuture;

public class UniRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions, String cacheControl) {
        super(router, filters, handleOptions, cacheControl);
    }

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions) {
        super(router, filters, handleOptions, null);
    }

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters) {
        super(router, filters, true, null);
    }

    @Override
    protected CompletableFuture<Void> runHandlerAsync(Task<HttpContext> handler, HttpContext context) {
        if (handler.isAsync() || handler.isUni()) {
            return handler.runAsync(context);
        }
        try {
            handler.run(context);
        } catch (Throwable e) {
            Exceptions.throwAny(e);
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
