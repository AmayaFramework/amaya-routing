package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class ArrayMethodMap implements MethodMap {
    private static final int METHOD_COUNT = HttpMethod.all().size();

    public static int ordinal(HttpMethod method) {
        if (method == HttpMethod.GET) return 0;
        if (method == HttpMethod.HEAD) return 1;
        if (method == HttpMethod.POST) return 2;
        if (method == HttpMethod.PUT) return 3;
        if (method == HttpMethod.DELETE) return 4;
        if (method == HttpMethod.CONNECT) return 5;
        if (method == HttpMethod.OPTIONS) return 6;
        if (method == HttpMethod.TRACE) return 7;
        if (method == HttpMethod.PATCH) return 8;
        throw new IllegalArgumentException("Unknown method: " + method);
    }

    private final Task[] handlers;
    private final Set<HttpMethod> methodSet;
    private final Set<HttpMethod> methodSetView;

    public ArrayMethodMap() {
        this.handlers = new Task[METHOD_COUNT];
        this.methodSet = new HashSet<>();
        this.methodSetView = Collections.unmodifiableSet(methodSet);
    }

    @Override
    public Task<HttpContext> get(HttpMethod method) {
        return handlers[ordinal(method)];
    }

    @Override
    public void put(HttpMethod method, Task<HttpContext> handler) {
        handlers[ordinal(method)] = handler;
        if (handler == null) {
            methodSet.remove(method);
        } else {
            methodSet.add(method);
        }
    }

    @Override
    public Task<HttpContext> remove(HttpMethod method) {
        var ret = handlers[ordinal(method)];
        if (ret == null) {
            return null;
        }
        handlers[ordinal(method)] = null;
        methodSet.remove(method);
        return ret;
    }

    @Override
    public Set<HttpMethod> methods() {
        return methodSetView;
    }
}
