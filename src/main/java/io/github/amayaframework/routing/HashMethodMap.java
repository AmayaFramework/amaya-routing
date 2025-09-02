package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class HashMethodMap implements MethodMap {
    private final Map<HttpMethod, Task<HttpContext>> methods;
    private final Set<HttpMethod> methodSetView;

    public HashMethodMap(Map<HttpMethod, Task<HttpContext>> methods) {
        this.methods = methods;
        this.methodSetView = Collections.unmodifiableSet(methods.keySet());
    }

    @Override
    public Task<HttpContext> get(HttpMethod method) {
        return methods.get(method);
    }

    @Override
    public void put(HttpMethod method, Task<HttpContext> handler) {
        methods.put(method, handler);
    }

    @Override
    public Set<HttpMethod> methods() {
        return methodSetView;
    }
}
