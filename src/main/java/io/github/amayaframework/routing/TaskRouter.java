package io.github.amayaframework.routing;

import com.github.romanqed.juni.Uni;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.Router;

import java.util.function.Supplier;

/**
 * A specialized {@link Router} that resolves HTTP routes into {@link MethodMap} instances
 * and integrates with the {@link Uni} execution model.
 * <p>
 * This router type is responsible for mapping request paths to sets of handlers grouped
 * by HTTP method, enabling fine-grained routing and middleware composition. It also
 * exposes information about its execution capabilities (sync, async, or uni).
 */
public interface TaskRouter extends Router<MethodMap>, Uni {

    /**
     * Resolves the given path into a {@link PathContext} containing a {@link MethodMap}.
     * <p>
     * The {@code supplier} provides an iterable over the path segments to avoid
     * unnecessary allocations when path parsing has already been performed.
     *
     * @param path     the raw request path
     * @param supplier a supplier of path segments
     * @return a {@link PathContext} with the matched route and associated method map,
     *         or {@code null} if no route matches
     */
    @Override
    PathContext<MethodMap> process(String path, Supplier<Iterable<String>> supplier);

    /**
     * Resolves the given path into a {@link PathContext} containing a {@link MethodMap}.
     *
     * @param path the raw request path
     * @return a {@link PathContext} with the matched route and associated method map,
     *         or {@code null} if no route matches
     */
    @Override
    PathContext<MethodMap> process(String path);

    /**
     * Checks whether this router contains no registered routes.
     *
     * @return {@code true} if the router is empty, {@code false} otherwise
     */
    boolean empty();

    /**
     * Indicates whether handlers of this router natively support synchronous execution.
     *
     * @return {@code true} if the router is synchronous
     */
    @Override
    boolean isSync();

    /**
     * Indicates whether handlers of this router natively support asynchronous execution.
     *
     * @return {@code true} if the router is asynchronous
     */
    @Override
    boolean isAsync();

    /**
     * Indicates whether handlers of this router conform to the unified execution model
     * represented by {@link Uni}.
     *
     * @return {@code true} if the router is uni-compatible
     */
    @Override
    boolean isUni();
}
