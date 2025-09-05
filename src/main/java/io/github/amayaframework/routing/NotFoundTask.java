package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpCode;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public final class NotFoundTask implements TaskConsumer<HttpContext> {
    private final String message;

    /**
     *
     * @param message
     */
    public NotFoundTask(String message) {
        this.message = message;
    }

    /**
     *
     */
    public NotFoundTask() {
        this.message = "Path not found";
    }

    @Override
    public void run(HttpContext context, Task<HttpContext> next) throws Throwable {
        context.response().sendError(HttpCode.NOT_FOUND, message);
    }

    @Override
    public CompletableFuture<Void> runAsync(HttpContext context, Task<HttpContext> next) {
        try {
            context.response().sendError(HttpCode.NOT_FOUND, message);
            return CompletableFuture.completedFuture(null);
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
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
