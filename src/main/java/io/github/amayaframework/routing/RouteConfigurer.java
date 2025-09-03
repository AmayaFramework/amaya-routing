package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskConfigurer;
import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.function.Consumer;

public interface RouteConfigurer extends Resettable {

    TaskConfigurer<HttpContext> map(HttpMethod method);

    default TaskConfigurer<HttpContext> get() {
        return map(HttpMethod.GET);
    }

    default TaskConfigurer<HttpContext> post() {
        return map(HttpMethod.POST);
    }

    RouteConfigurer map(HttpMethod method, Consumer<TaskConfigurer<HttpContext>> action);

    default RouteConfigurer get(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.GET, action);
    }

    default RouteConfigurer post(Consumer<TaskConfigurer<HttpContext>> action) {
        return map(HttpMethod.POST, action);
    }

    RouteConfigurer map(HttpMethod method, Task<HttpContext> task);

    default RouteConfigurer get(Task<HttpContext> task) {
        return map(HttpMethod.GET, task);
    }

    default RouteConfigurer post(Task<HttpContext> task) {
        return map(HttpMethod.POST, task);
    }

    default RouteConfigurer map(HttpMethod method, SyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    default RouteConfigurer map(HttpMethod method, AsyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    default RouteConfigurer get(SyncTask<HttpContext> task) {
        return map(HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouteConfigurer get(AsyncTask<HttpContext> task) {
        return map(HttpMethod.GET, (Task<HttpContext>) task);
    }

    default RouteConfigurer post(SyncTask<HttpContext> task) {
        return map(HttpMethod.POST, (Task<HttpContext>) task);
    }

    default RouteConfigurer post(AsyncTask<HttpContext> task) {
        return map(HttpMethod.POST, (Task<HttpContext>) task);
    }

    RouteConfigurer unmap(HttpMethod method);
}
