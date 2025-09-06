package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A {@link Map}-backed implementation of {@link MethodMap}.
 * <p>
 * This implementation delegates all operations to the provided {@link Map},
 * making it flexible in terms of internal storage. The behavior depends on
 * the specific {@link Map} implementation used (e.g., {@link java.util.HashMap},
 * {@link java.util.concurrent.ConcurrentHashMap}).
 * <p>
 * Lookups, insertions, and removals have the same complexity as the underlying
 * {@link Map}. The set of registered methods is exposed as an unmodifiable view.
 */
public final class HashMethodMap implements MethodMap {
    private final Map<HttpMethod, Task<HttpContext>> methods;
    private final Set<HttpMethod> methodSetView;

    /**
     * Constructs a {@link HashMethodMap} backed by the given {@link Map}.
     * <p>
     * The provided map is used directly without copying, so external
     * modifications will be reflected in this instance.
     *
     * @param methods the backing map to store method-handler associations
     */
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
