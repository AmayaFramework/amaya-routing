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

    RouteConfigurer map(HttpMethod method, Consumer<TaskConfigurer<HttpContext>> action);

    RouteConfigurer map(HttpMethod method, Task<HttpContext> task);

    default RouteConfigurer map(HttpMethod method, SyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    default RouteConfigurer map(HttpMethod method, AsyncTask<HttpContext> task) {
        return map(method, (Task<HttpContext>) task);
    }

    RouteConfigurer unmap(HttpMethod method);
}
