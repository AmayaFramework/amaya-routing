package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConfigurer;
import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.path.Path;

/**
 * Defines a fluent interface for configuring an HTTP router.
 * <p>
 * A router maps URL paths to {@link RouteConfigurer routes}, which in turn
 * define method-specific pipelines of tasks. Routes can be defined either
 * using {@link Path} objects or string path patterns.
 * <p>
 * Tasks may be registered directly as {@link Task}, {@link SyncTask}, or {@link AsyncTask},
 * or assembled through {@link TaskConfigurer} pipelines.
 * <p>
 * This configurer also provides convenient {@link #get(Path)}, {@link #post(Path)},
 * {@link #get(String)}, and {@link #post(String)} methods for common cases.
 */
public interface RouterConfigurer extends Resettable {

    /**
     * Returns a {@link RouteConfigurer} for the given path.
     *
     * @param path the path to configure
     * @return a route configurer for the specified path
     */
    RouteConfigurer map(Path path);

    /**
     * Returns a {@link RouteConfigurer} for the given string path.
     *
     * @param path the path pattern to configure
     * @return a route configurer for the specified path
     */
    RouteConfigurer map(String path);

    /**
     * Returns a pipeline configurer for the given path and HTTP method.
     *
     * @param path   the path to configure
     * @param method the HTTP method to map
     * @return a pipeline configurer for the given path and method
     */
    default TaskConfigurer<HttpContext> map(Path path, HttpMethod method) {
        return map(path).map(method);
    }

    /**
     * Returns a pipeline configurer for a {@code GET} request at the given path.
     *
     * @param path the path to configure
     * @return a pipeline configurer for GET
     */
    default TaskConfigurer<HttpContext> get(Path path) {
        return map(path, HttpMethod.GET);
    }

    /**
     * Returns a pipeline configurer for a {@code POST} request at the given path.
     *
     * @param path the path to configure
     * @return a pipeline configurer for POST
     */
    default TaskConfigurer<HttpContext> post(Path path) {
        return map(path, HttpMethod.POST);
    }

    /**
     * Returns a pipeline configurer for the given string path and HTTP method.
     *
     * @param path   the string path to configure
     * @param method the HTTP method to map
     * @return a pipeline configurer for the given path and method
     */
    TaskConfigurer<HttpContext> map(String path, HttpMethod method);

    /**
     * Returns a pipeline configurer for a {@code GET} request at the given string path.
     *
     * @param path the string path to configure
     * @return a pipeline configurer for GET
     */
    default TaskConfigurer<HttpContext> get(String path) {
        return map(path, HttpMethod.GET);
    }

    /**
     * Returns a pipeline configurer for a {@code POST} request at the given string path.
     *
     * @param path the string path to configure
     * @return a pipeline configurer for POST
     */
    default TaskConfigurer<HttpContext> post(String path) {
        return map(path, HttpMethod.POST);
    }

    /**
     * Maps a specific task to the given path and HTTP method.
     *
     * @param path   the path to configure
     * @param method the HTTP method to map
     * @param task   the task to execute
     * @return this configurer
     */
    default RouterConfigurer map(Path path, HttpMethod method, Task<HttpContext> task) {
        map(path).map(method, task);
        return this;
    }

    /**
     * Maps a {@code GET} task to the given path.
     *
     * @param path the path to configure
     * @param task the task to execute
     * @return this configurer
     */
    default RouterConfigurer get(Path path, Task<HttpContext> task) {
        map(path).map(HttpMethod.GET, task);
        return this;
    }

    /**
     * Maps a {@code POST} task to the given path.
     *
     * @param path the path to configure
     * @param task the task to execute
     * @return this configurer
     */
    default RouterConfigurer post(Path path, Task<HttpContext> task) {
        map(path).map(HttpMethod.POST, task);
        return this;
    }

    /**
     * Maps a synchronous task to the given path and HTTP method.
     *
     * @param path   the path to configure
     * @param method the HTTP method to map
     * @param task   the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer map(Path path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous task to the given path and HTTP method.
     *
     * @param path   the path to configure
     * @param method the HTTP method to map
     * @param task   the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer map(Path path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    /**
     * Maps a synchronous {@code GET} task to the given path.
     *
     * @param path the path to configure
     * @param task the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer get(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous {@code GET} task to the given path.
     *
     * @param path the path to configure
     * @param task the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer get(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps a synchronous {@code POST} task to the given path.
     *
     * @param path the path to configure
     * @param task the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer post(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous {@code POST} task to the given path.
     *
     * @param path the path to configure
     * @param task the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer post(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Maps a specific task to the given string path and HTTP method.
     *
     * @param path   the string path to configure
     * @param method the HTTP method to map
     * @param task   the task to execute
     * @return this configurer
     */
    RouterConfigurer map(String path, HttpMethod method, Task<HttpContext> task);

    /**
     * Maps a {@code GET} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the task to execute
     * @return this configurer
     */
    default RouterConfigurer get(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    /**
     * Maps a {@code POST} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the task to execute
     * @return this configurer
     */
    default RouterConfigurer post(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    /**
     * Maps a synchronous task to the given string path and HTTP method.
     *
     * @param path   the string path to configure
     * @param method the HTTP method to map
     * @param task   the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer map(String path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous task to the given string path and HTTP method.
     *
     * @param path   the string path to configure
     * @param method the HTTP method to map
     * @param task   the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer map(String path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    /**
     * Maps a synchronous {@code GET} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer get(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous {@code GET} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer get(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps a synchronous {@code POST} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the synchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer post(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Maps an asynchronous {@code POST} task to the given string path.
     *
     * @param path the string path to configure
     * @param task the asynchronous task to execute
     * @return this configurer
     */
    default RouterConfigurer post(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Removes the mapping for the given path.
     *
     * @param path the path to unmap
     * @return this configurer
     */
    RouterConfigurer unmap(Path path);

    /**
     * Removes the mapping for the given string path.
     *
     * @param path the string path to unmap
     * @return this configurer
     */
    RouterConfigurer unmap(String path);

    /**
     * Removes the mapping for the given path and HTTP method.
     *
     * @param path   the path to unmap
     * @param method the HTTP method to unmap
     * @return this configurer
     */
    RouterConfigurer unmap(Path path, HttpMethod method);

    /**
     * Removes the mapping for the given string path and HTTP method.
     *
     * @param path   the string path to unmap
     * @param method the HTTP method to unmap
     * @return this configurer
     */
    RouterConfigurer unmap(String path, HttpMethod method);
}
