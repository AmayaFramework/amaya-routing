package io.github.amayaframework.routing;

import io.github.amayaframework.router.PathContext;

import java.util.function.Supplier;

/**
 * A {@link TaskRouter} implementation that performs no routing.
 * <p>
 * This router always returns {@code null} for route resolution and
 * reports itself as empty. It is typically used as a placeholder
 * or default value when no real router is configured.
 * <p>
 * The singleton instance is exposed via {@link #INSTANCE} and
 * the convenience method {@link #emptyRouter()}.
 */
public final class EmptyRouter implements TaskRouter {

    /**
     * The singleton instance of an empty router.
     */
    public static final TaskRouter INSTANCE = new EmptyRouter();

    /**
     * Returns the singleton instance of an empty router.
     *
     * @return the shared {@link EmptyRouter} instance
     */
    public static TaskRouter emptyRouter() {
        return INSTANCE;
    }

    private EmptyRouter() {
    }

    /**
     * Always returns {@code null}, since no routes are defined.
     *
     * @param path     the request path
     * @param supplier a supplier of path segments
     * @return always {@code null}
     */
    @Override
    public PathContext<MethodMap> process(String path, Supplier<Iterable<String>> supplier) {
        return null;
    }

    /**
     * Always returns {@code null}, since no routes are defined.
     *
     * @param path the request path
     * @return always {@code null}
     */
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
