package io.github.amayaframework.routing;

import com.github.romanqed.jconv.LinkedTaskBuilder;
import com.github.romanqed.jconv.Task;
import com.github.romanqed.jconv.TaskBuilder;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RouteBuilder extends AbstractRouteConfigurer<RouteBuilder> {
    protected final Supplier<MethodMap> supplier;
    protected final boolean preferExtended;

    public RouteBuilder(Supplier<MethodMap> supplier, boolean preferExtended) {
        this.supplier = supplier;
        this.preferExtended = preferExtended;
    }

    public RouteBuilder() {
        this.supplier = IdentityMethodMap::new;
        this.preferExtended = false;
    }

    @Override
    protected TaskBuilder<HttpContext> createTaskBuilder() {
        return new LinkedTaskBuilder<>();
    }

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

    public MethodMap build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
