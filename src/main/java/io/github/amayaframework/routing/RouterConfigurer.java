package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConfigurer;
import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.path.Path;

public interface RouterConfigurer extends Resettable {

    RouteConfigurer map(Path path);

    RouteConfigurer map(String path);

    default TaskConfigurer<HttpContext> map(Path path, HttpMethod method) {
        return map(path).map(method);
    }

    default TaskConfigurer<HttpContext> get(Path path) {
        return map(path, HttpMethod.GET);
    }

    default TaskConfigurer<HttpContext> post(Path path) {
        return map(path, HttpMethod.POST);
    }

    TaskConfigurer<HttpContext> map(String path, HttpMethod method);

    default TaskConfigurer<HttpContext> get(String path) {
        return map(path, HttpMethod.GET);
    }

    default TaskConfigurer<HttpContext> post(String path) {
        return map(path, HttpMethod.POST);
    }

    default RouterConfigurer map(Path path, HttpMethod method, Task<HttpContext> task) {
        map(path).map(method, task);
        return this;
    }

    default RouterConfigurer get(Path path, Task<HttpContext> task) {
        map(path).map(HttpMethod.GET, task);
        return this;
    }

    default RouterConfigurer post(Path path, Task<HttpContext> task) {
        map(path).map(HttpMethod.POST, task);
        return this;
    }

    default RouterConfigurer map(Path path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer map(Path path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer get(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouterConfigurer get(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouterConfigurer post(Path path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    default RouterConfigurer post(Path path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    RouterConfigurer map(String path, HttpMethod method, Task<HttpContext> task);

    default RouterConfigurer get(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.GET, task);
    }

    default RouterConfigurer post(String path, Task<HttpContext> task) {
        return map(path, HttpMethod.POST, task);
    }

    default RouterConfigurer map(String path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer map(String path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer get(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouterConfigurer get(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouterConfigurer post(String path, SyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    default RouterConfigurer post(String path, AsyncTask<HttpContext> task) {
        return map(path, HttpMethod.POST, (Task<HttpContext>) task);
    }

    RouterConfigurer unmap(Path path);

    RouterConfigurer unmap(String path);

    RouterConfigurer unmap(Path path, HttpMethod method);

    RouterConfigurer unmap(String path, HttpMethod method);
}
