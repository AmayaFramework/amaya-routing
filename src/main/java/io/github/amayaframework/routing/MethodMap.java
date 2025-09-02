package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Set;

public interface MethodMap {

    Task<HttpContext> get(HttpMethod method);

    void put(HttpMethod method, Task<HttpContext> handler);

    Task<HttpContext> remove(HttpMethod method);

    Set<HttpMethod> methods();
}
