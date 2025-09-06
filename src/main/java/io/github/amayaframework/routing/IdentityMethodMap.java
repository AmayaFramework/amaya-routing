package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An {@link IdentityHashMap}-based implementation of {@link MethodMap}.
 * <p>
 * This implementation uses reference equality ({@code ==}) to compare
 * {@link HttpMethod} instances. It is efficient when methods are represented
 * as canonical singletons (e.g., enum-like constants).
 * <p>
 * All operations such as lookup, insertion, and removal are backed directly
 * by the underlying {@link IdentityHashMap}.
 */
public final class IdentityMethodMap implements MethodMap {
    private final IdentityHashMap<HttpMethod, Task<HttpContext>> methods;
    private final Set<HttpMethod> methodSetView;

    /**
     * Constructs an empty {@link IdentityMethodMap}.
     * <p>
     * The internal {@link IdentityHashMap} is initialized with default capacity.
     * The set of registered methods is exposed as an unmodifiable view.
     */
    public IdentityMethodMap() {
        this.methods = new IdentityHashMap<>();
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
    public Task<HttpContext> remove(HttpMethod method) {
        return methods.remove(method);
    }

    @Override
    public boolean empty() {
        return methods.isEmpty();
    }

    @Override
    public Set<HttpMethod> methods() {
        return methodSetView;
    }

    @Override
    public void forEach(Consumer<Task<HttpContext>> consumer) {
        methods.values().forEach(consumer);
    }

    @Override
    public void forEach(BiConsumer<HttpMethod, Task<HttpContext>> consumer) {
        methods.forEach(consumer);
    }
}
