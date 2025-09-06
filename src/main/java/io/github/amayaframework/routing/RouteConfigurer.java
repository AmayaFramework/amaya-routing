package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConfigurer;
import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.function.Consumer;

/**
 * Defines a fluent interface for configuring routes and their associated task pipelines.
 * <p>
 * Each HTTP method can be mapped to a {@link TaskConfigurer} that builds the processing
 * pipeline for requests using that method. Convenience methods are provided for common
 * verbs such as {@link #get()} and {@link #post()}.
 * <p>
 * Tasks can be registered directly as {@link Task}, {@link SyncTask}, or {@link AsyncTask},
 * or entire pipelines can be configured using {@link TaskConfigurer}.
 * <p>
 * Implementations are expected to maintain mappings between {@link HttpMethod} values
 * and their corresponding processing chains.
 */
public interface RouteConfigurer extends Resettable {

    /**
     * Returns the pipeline configurer for the given HTTP method.
     *
     * @param method the HTTP method to configure
     * @return a {@link TaskConfigurer} for building the pipeline
     */
    TaskConfigurer<HttpContext> map(HttpMethod method);

    /**
     * Returns the pipeline configurer for {@link HttpMethod#GET}.
     *
     * @return a {@link TaskConfigurer} for building the GET pipeline
     */
    default TaskConfigurer<HttpContext> get() {
        return map(HttpMethod.GET);
    }

    /**
     * Returns the pipeline configurer for {@link HttpMethod#POST}.
     *
     * @return a {@link TaskConfigurer} for building the POST pipeline
     */
    default TaskConfigurer<HttpContext> post() {
        return map(HttpMethod.POST);
    }

    /**
     * Configures the pipeline for the given HTTP method using the provided action.
     *
     * @param method the HTTP method to configure
     * @param action a consumer that assembles the pipeline
     * @return this configurer
     */
    RouteConfigurer map(HttpMethod method, Consumer<TaskConfigurer<HttpContext>> action);

    /**
     * Configures the pipeline for {@link HttpMethod#GET} using the provided action.
     *
     * @param action a consumer that assembles the GET pipeline
     * @return this configurer
     */
    default RouteConfigurer get(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.GET, action);
    }

    /**
     * Configures the pipeline for {@link HttpMethod#POST} using the provided action.
     *
     * @param action a consumer that assembles the POST pipeline
     * @return this configurer
     */
    default RouteConfigurer post(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.POST, action);
    }

    /**
     * Maps the given HTTP method directly to a single task.
     *
     * @param method the HTTP method to map
     * @param task   the task to associate
     * @return this configurer
     */
    RouteConfigurer map(HttpMethod method, Task<HttpContext> task);

    /**
     * Maps {@link HttpMethod#GET} to a single task.
     *
     * @param task the task to associate with GET
     * @return this configurer
     */
    default RouteConfigurer get(Task<HttpContext> task) {
        return map(HttpMethod.GET, task);
    }

    /**
     * Maps {@link HttpMethod#POST} to a single task.
     *
     * @param task the task to associate with POST
     * @return this configurer
     */
    default RouteConfigurer post(Task<HttpContext> task) {
        return map(HttpMethod.POST, task);
    }

    /**
     * Maps the given HTTP method to a synchronous task.
     *
     * @param method the HTTP method to map
     * @param task   the synchronous task to associate
     * @return this configurer
     */
    default RouteConfigurer map(HttpMethod method, SyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    /**
     * Maps the given HTTP method to an asynchronous task.
     *
     * @param method the HTTP method to map
     * @param task   the asynchronous task to associate
     * @return this configurer
     */
    default RouteConfigurer map(HttpMethod method, AsyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    /**
     * Maps {@link HttpMethod#GET} to a synchronous task.
     *
     * @param task the synchronous task to associate with GET
     * @return this configurer
     */
    default RouteConfigurer get(SyncTask<HttpContext> task) {
        return map(HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps {@link HttpMethod#GET} to an asynchronous task.
     *
     * @param task the asynchronous task to associate with GET
     * @return this configurer
     */
    default RouteConfigurer get(AsyncTask<HttpContext> task) {
        return map(HttpMethod.GET, (Task<HttpContext>) task);
    }

    /**
     * Maps {@link HttpMethod#POST} to a synchronous task.
     *
     * @param task the synchronous task to associate with POST
     * @return this configurer
     */
    default RouteConfigurer post(SyncTask<HttpContext> task) {
        return map(HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Maps {@link HttpMethod#POST} to an asynchronous task.
     *
     * @param task the asynchronous task to associate with POST
     * @return this configurer
     */
    default RouteConfigurer post(AsyncTask<HttpContext> task) {
        return map(HttpMethod.POST, (Task<HttpContext>) task);
    }

    /**
     * Removes the mapping for the given HTTP method.
     *
     * @param method the HTTP method to unmap
     * @return this configurer
     */
    RouteConfigurer unmap(HttpMethod method);
}
