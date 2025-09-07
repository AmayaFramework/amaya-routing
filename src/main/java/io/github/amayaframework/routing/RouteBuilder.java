package io.github.amayaframework.routing;

import com.github.romanqed.jconv.LinkedTaskBuilder;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskBuilder;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A builder for constructing {@link MethodMap} instances that associate
 * HTTP methods with request handling pipelines.
 * <p>
 * This builder extends {@link AbstractRouteConfigurer}, allowing routes to be
 * configured fluently with tasks or task pipelines. It supports two storage
 * strategies:
 * <ul>
 *   <li>{@link ArrayMethodMap} for standard {@link HttpMethod} values.</li>
 *   <li>A custom map supplied by {@link Supplier}, for extended or non-standard methods.</li>
 * </ul>
 * <p>
 * The final {@link MethodMap} is produced via {@link #build()}, after which
 * the builder state is reset and can be reused.
 */
public class RouteBuilder extends AbstractRouteConfigurer<RouteBuilder> {
    protected final Supplier<MethodMap> supplier;
    protected final boolean preferExtended;

    /**
     * Creates a new route builder.
     *
     * @param supplier       a supplier for creating extended {@link MethodMap} instances
     * @param preferExtended whether to prefer the extended method map over the default {@link ArrayMethodMap}
     */
    public RouteBuilder(Supplier<MethodMap> supplier, boolean preferExtended) {
        this.supplier = supplier;
        this.preferExtended = preferExtended;
    }

    /**
     * Creates a new route builder with default settings.
     * <p>
     * Uses {@link IdentityMethodMap} for extended maps and
     * does not prefer extended storage unless required.
     */
    public RouteBuilder() {
        this.supplier = IdentityMethodMap::new;
        this.preferExtended = false;
    }

    @Override
    protected TaskBuilder<HttpContext> createTaskBuilder() {
        return new LinkedTaskBuilder<>();
    }

    /**
     * Creates a {@link MethodMap} using either the supplier or a default
     * {@link ArrayMethodMap}, depending on whether extended methods are required.
     *
     * @param extended whether to use the extended map
     * @return a new method map
     */
    protected MethodMap createMethodMap(boolean extended) {
        if (extended) {
            return supplier.get();
        }
        return new ArrayMethodMap();
    }

    private Map<HttpMethod, Task<HttpContext>> buildMap() {
        if (tasks == null && builders == null) {
            return null;
        }
        var ret = tasks == null ? new HashMap<HttpMethod, Task<HttpContext>>() : tasks;
        if (builders != null) {
            builders.forEach((method, builder) -> ret.put(method, builder.build()));
        }
        return ret;
    }

    /**
     * Builds a {@link MethodMap} from the current configuration without resetting state.
     * <p>
     * If no tasks are defined, returns an empty {@link ArrayMethodMap} or an extended map
     * depending on {@link #preferExtended}. Extended methods are detected automatically.
     *
     * @return a method map for the configured routes
     */
    protected MethodMap doBuild() {
        var map = buildMap();
        if (map == null || map.isEmpty()) {
            return createMethodMap(preferExtended);
        }
        var hasExtended = false;
        for (var entry : map.entrySet()) {
            var method = entry.getKey();
            var found = HttpMethod.of(method.getName());
            if (method == found) {
                continue;
            }
            if (found == null) {
                hasExtended = true;
                continue;
            }
            map.put(found, entry.getValue());
        }
        var ret = createMethodMap(hasExtended || preferExtended);
        map.forEach(ret::put);
        return ret;
    }

    /**
     * Builds a {@link MethodMap} and resets this builder.
     *
     * @return the constructed method map
     */
    public MethodMap build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
