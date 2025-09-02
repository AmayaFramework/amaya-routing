package io.github.amayaframework.routing;

import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface MethodMap {

    Task<HttpContext> get(HttpMethod method);

    void put(HttpMethod method, Task<HttpContext> handler);

    Task<HttpContext> remove(HttpMethod method);

    boolean empty(); // новое

    Set<HttpMethod> methods();

    void forEach(Consumer<Task<HttpContext>> consumer); // новое

    void forEach(BiConsumer<HttpMethod, Task<HttpContext>> consumer); // новое
}
