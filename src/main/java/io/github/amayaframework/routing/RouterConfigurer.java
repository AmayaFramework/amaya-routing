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

    TaskConfigurer<HttpContext> map(String path, HttpMethod method);

    default RouterConfigurer map(Path path, HttpMethod method, Task<HttpContext> task) {
        map(path).map(method, task);
        return this;
    }

    default RouterConfigurer map(Path path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer map(Path path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    RouterConfigurer map(String path, HttpMethod method, Task<HttpContext> task);

    default RouterConfigurer map(String path, HttpMethod method, SyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    default RouterConfigurer map(String path, HttpMethod method, AsyncTask<HttpContext> task) {
        return map(path, method, (Task<HttpContext>) task);
    }

    RouterConfigurer unmap(Path path);

    RouterConfigurer unmap(String path);

    RouterConfigurer unmap(Path path, HttpMethod method);

    RouterConfigurer unmap(String path, HttpMethod method);
}
