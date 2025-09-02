package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public static HttpMethod of(int ordinal) {
        switch (ordinal) {
            case 0:
                return HttpMethod.GET;
            case 1:
                return HttpMethod.HEAD;
            case 2:
                return HttpMethod.POST;
            case 3:
                return HttpMethod.PUT;
            case 4:
                return HttpMethod.DELETE;
            case 5:
                return HttpMethod.CONNECT;
            case 6:
                return HttpMethod.OPTIONS;
            case 7:
                return HttpMethod.TRACE;
            case 8:
                return HttpMethod.PATCH;
            default:
                throw new IllegalArgumentException("Unknown ordinal: " + ordinal);
        }
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
    public boolean empty() {
        return methodSet.isEmpty();
    }

    @Override
    public Set<HttpMethod> methods() {
        return methodSetView;
    }

    @Override
    public void forEach(Consumer<Task<HttpContext>> consumer) {
        for (var handler : handlers) {
            if (handler != null) {
                consumer.accept(handler);
            }
        }
    }

    @Override
    public void forEach(BiConsumer<HttpMethod, Task<HttpContext>> consumer) {
        for (var i = 0; i < handlers.length; ++i) {
            var handler = handlers[i];
            if (handler == null) {
                continue;
            }
            consumer.accept(of(i), handler);
        }
    }
}
