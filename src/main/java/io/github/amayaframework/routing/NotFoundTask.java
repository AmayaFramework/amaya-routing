package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpCode;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link TaskConsumer} that always produces a {@code 404 Not Found} response.
 * <p>
 * This task is typically used as a fallback handler when no matching route
 * is found in the router. It can operate in both synchronous and asynchronous
 * execution modes and is marked as unified.
 */
public final class NotFoundTask implements TaskConsumer<HttpContext> {
    private final String message;

    /**
     * Creates a new {@code NotFoundTask} with a custom error message.
     *
     * @param message the error message to include in the {@code 404} response
     */
    public NotFoundTask(String message) {
        this.message = message;
    }

    /**
     * Creates a new {@code NotFoundTask} with the default message:
     * {@code "Path not found"}.
     */
    public NotFoundTask() {
        this.message = "Path not found";
    }

    /**
     * Sends a {@code 404 Not Found} response synchronously.
     *
     * @param context the current HTTP context
     * @param next    the continuation task (ignored in this implementation)
     * @throws Throwable if the response cannot be sent
     */
    @Override
    public void run(HttpContext context, Task<HttpContext> next) throws Throwable {
        context.response().sendError(HttpCode.NOT_FOUND, message);
    }

    /**
     * Sends a {@code 404 Not Found} response asynchronously.
     *
     * @param context the current HTTP context
     * @param next    the continuation task (ignored in this implementation)
     * @return a completed future when the response is sent,
     * or a failed future if sending fails
     */
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
