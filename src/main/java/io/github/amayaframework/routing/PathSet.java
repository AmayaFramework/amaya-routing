package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

/**
 * An interface describing an abstract set of http paths associated with http methods and handlers.
 */
public interface PathSet {

    /**
     * Gets handler associated with given http method and path.
     *
     * @param method the specified http method, must be non-null
     * @param path   the specified path, must be non-null
     * @return the {@link Runnable1} instance if found, null otherwise
     */
    Runnable1<HttpContext> get(HttpMethod method, String path);

    /**
     * Sets a handler for the specified http method and path.
     *
     * @param method  the specified http method, must be non-null
     * @param path    the specified path, must be non-null
     * @param handler the handler to be associated with the specified method and path, must be non-null
     */
    void set(HttpMethod method, String path, Runnable1<HttpContext> handler);

    /**
     * Removes the handler associated with the specified http method and path.
     *
     * @param method the specified HTTP method, must be non-null
     * @param path   the specified path, must be non-null
     */
    void remove(HttpMethod method, String path);

    /**
     * Checks if there are any handlers registered in this path set.
     *
     * @return true if there are no handlers registered, false otherwise
     */
    boolean isEmpty();

    /**
     * Clears all handlers from this path set.
     */
    void clear();
}
