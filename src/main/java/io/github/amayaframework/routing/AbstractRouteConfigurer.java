package io.github.amayaframework.routing;

import com.github.romanqed.jconv.*;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractRouteConfigurer<C extends RouteConfigurer> implements RouteConfigurer {
    protected Map<HttpMethod, Task<HttpContext>> tasks;
    protected Map<HttpMethod, TaskBuilder<HttpContext>> builders;

    protected abstract TaskBuilder<HttpContext> createTaskBuilder();

    protected void ensureTasks() {
        if (tasks == null) {
            tasks = new HashMap<>();
        }
    }

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
    public C map(HttpMethod method, SyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    @Override
    public C map(HttpMethod method, AsyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
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
