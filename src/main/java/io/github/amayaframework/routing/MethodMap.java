package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A container mapping {@link HttpMethod HTTP methods} to their corresponding handlers.
 * <p>
 * Provides efficient lookup and iteration over the handlers registered for a given route.
 * Each handler is represented by a {@link Task} that operates on an {@link HttpContext}.
 */
public interface MethodMap {

    /**
     * Returns the handler associated with the given HTTP method.
     *
     * @param method the HTTP method to resolve
     * @return the handler task for the method, or {@code null} if not present
     */
    Task<HttpContext> get(HttpMethod method);

    /**
     * Associates the given handler with the specified HTTP method.
     * If a handler already exists for the method, it will be replaced.
     *
     * @param method  the HTTP method to map
     * @param handler the handler task to associate
     */
    void put(HttpMethod method, Task<HttpContext> handler);

    /**
     * Removes the handler associated with the given HTTP method.
     *
     * @param method the HTTP method whose handler should be removed
     * @return the previously associated handler, or {@code null} if none was present
     */
    Task<HttpContext> remove(HttpMethod method);

    /**
     * Checks whether no handlers are registered.
     *
     * @return {@code true} if the map contains no handlers, {@code false} otherwise
     */
    boolean empty();

    /**
     * Returns the set of all HTTP methods currently registered.
     *
     * @return a set of supported methods
     */
    Set<HttpMethod> methods();

    /**
     * Iterates over all registered handlers.
     *
     * @param consumer a consumer that accepts each handler
     */
    void forEach(Consumer<Task<HttpContext>> consumer);

    /**
     * Iterates over all method-handler pairs.
     *
     * @param consumer a consumer that accepts each method and its handler
     */
    void forEach(BiConsumer<HttpMethod, Task<HttpContext>> consumer);
}
