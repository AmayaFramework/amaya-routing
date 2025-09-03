package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConfigurer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.parser.PathParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractRouterConfigurer<C extends RouterConfigurer, R extends RouteConfigurer> implements RouterConfigurer {
    protected final PathParser parser;
    protected Map<Path, R> paths;
    protected Map<String, Path> parsed;

    protected AbstractRouterConfigurer(PathParser parser) {
        this.parser = parser;
    }

    protected abstract R createRouteConfigurer();

    protected Path parse(String path) {
        if (path == null) {
            return null;
        }
        if (parsed == null) {
            parsed = new HashMap<>();
        }
        return parsed.computeIfAbsent(path, parser::parse);
    }

    protected void ensurePaths() {
        if (paths == null) {
            paths = new HashMap<>();
        }
    }

    @Override
    public void reset() {
        paths = null;
        parsed = null;
    }

    @Override
    public RouteConfigurer map(Path path) {
        Objects.requireNonNull(path);
        ensurePaths();
        return paths.computeIfAbsent(path, p -> createRouteConfigurer());
    }

    @Override
    public RouteConfigurer map(String path) {
        return map(parse(path));
    }

    @Override
    public TaskConfigurer<HttpContext> map(Path path, HttpMethod method) {
        return map(path).map(method);
    }

    @Override
    public TaskConfigurer<HttpContext> map(String path, HttpMethod method) {
        return map(parse(path), method);
    }

    @Override
    @SuppressWarnings("unchecked")
    public C map(Path path, HttpMethod method, Task<HttpContext> task) {
        map(path).map(method, task);
        return (C) this;
    }

    @Override
    public C get(Path path, Task<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C post(Path path, Task<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    public C map(Path path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    @Override
    public C map(Path path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    @Override
    public C get(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C get(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C post(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    public C post(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    public C map(String path, HttpMethod method, Task<HttpContext> task) {
        return map(parse(path), method, task);
    }

    @Override
    public C get(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C post(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    public C map(String path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    @Override
    public C map(String path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    @Override
    public C get(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C get(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    @Override
    public C post(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    public C post(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    @Override
    @SuppressWarnings("unchecked")
    public C unmap(Path path) {
        if (path != null && paths != null) {
            paths.remove(path);
        }
        return (C) this;
    }

    @Override
    public C unmap(String path) {
        return unmap(parse(path));
    }

    @Override
    @SuppressWarnings("unchecked")
    public C unmap(Path path, HttpMethod method) {
        if (path == null || method == null || paths == null) {
            return (C) this;
        }
        var configurer = paths.get(path);
        if (configurer != null) {
            configurer.unmap(method);
        }
        return (C) this;
    }

    @Override
    public C unmap(String path, HttpMethod method) {
        return unmap(parse(path), method);
    }
}
