package io.github.amayaframework.routing;

import com.github.romanqed.jconv.*;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A skeletal implementation of {@link RouteConfigurer}.
 * <p>
 * This class provides storage and management of {@link Task tasks} and
 * {@link TaskBuilder task builders} for each {@link HttpMethod}.
 * <p>
 * Subclasses must implement {@link #createTaskBuilder()} to supply
 * the builder implementation used to construct method-specific pipelines.
 *
 * @param <C> the concrete self-type, allowing fluent API chaining
 */
public abstract class AbstractRouteConfigurer<C extends RouteConfigurer> implements RouteConfigurer {
    protected Map<HttpMethod, Task<HttpContext>> tasks;
    protected Map<HttpMethod, TaskBuilder<HttpContext>> builders;

    /**
     * Creates a new task builder for assembling pipelines.
     *
     * @return a new task builder
     */
    protected abstract TaskBuilder<HttpContext> createTaskBuilder();

    /**
     * Ensures that the task map exists.
     */
    protected void ensureTasks() {
        if (tasks == null) {
            tasks = new HashMap<>();
        }
    }

    /**
     * Ensures that the builder map exists.
     */
    protected void ensureBuilders() {
        if (builders == null) {
            builders = new HashMap<>();
        }
    }

    @Override
    public void reset() {
        tasks = null;
        builders = null;
    }

    @Override
    public TaskConfigurer<HttpContext> map(HttpMethod method) {
        Objects.requireNonNull(method);
        if (tasks != null) {
            tasks.remove(method);
        }
        ensureBuilders();
        return builders.computeIfAbsent(method, m -> createTaskBuilder());
    }

    @Override
    @SuppressWarnings("unchecked")
    public C map(HttpMethod method, Consumer<TaskConfigurer<HttpContext>> action) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(action);
        var builder = createTaskBuilder();
        action.accept(builder);
        if (builders != null) {
            builders.remove(method);
        }
        ensureTasks();
        tasks.put(method, builder.build());
        return (C) this;
    }

    @Override
    public C get(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.GET, action);
    }

    @Override
    public C post(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.POST, action);
    }

    @Override
    @SuppressWarnings("unchecked")
    public C map(HttpMethod method, Task<HttpContext> task) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(task);
        if (builders != null) {
            builders.remove(method);
        }
        ensureTasks();
        tasks.put(method, task);
        return (C) this;
    }

    @Override
    public C get(Task<HttpContext> task) {
        return map(HttpMethod.GET, task);
    }

    @Override
    public C post(Task<HttpContext> task) {
        return map(HttpMethod.POST, task);
    }

    @Override
    public C map(HttpMethod method, SyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    @Override
    public C map(HttpMethod method, AsyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    @Override
    public C get(SyncTask<HttpContext> task) {
        return map(HttpMethod.GET, task);
    }

    @Override
    public C get(AsyncTask<HttpContext> task) {
        return map(HttpMethod.GET, task);
    }

    @Override
    public C post(SyncTask<HttpContext> task) {
        return map(HttpMethod.POST, task);
    }

    @Override
    public C post(AsyncTask<HttpContext> task) {
        return map(HttpMethod.POST, task);
    }

    @Override
    @SuppressWarnings("unchecked")
    public C unmap(HttpMethod method) {
        if (tasks != null) {
            tasks.remove(method);
        }
        if (builders != null) {
            builders.remove(method);
        }
        return (C) this;
    }
}
