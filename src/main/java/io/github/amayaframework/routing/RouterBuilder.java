package io.github.amayaframework.routing;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.parser.PathParser;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;

import java.util.HashMap;
import java.util.function.Supplier;

public class RouterBuilder extends AbstractRouterConfigurer<RouterBuilder, RouteBuilder> {
    protected final RouterFactory factory;
    protected final Supplier<RouteBuilder> supplier;

    public RouterBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        super(parser);
        this.factory = factory;
        this.supplier = supplier;
    }

    public RouterBuilder(PathParser parser, RouterFactory factory) {
        this(parser, factory, RouteBuilder::new);
    }

    public RouterBuilder(RouterFactory factory) {
        this(PathParsers.createDefault(), factory);
    }

    @Override
    protected RouteBuilder createRouteConfigurer() {
        return supplier.get();
    }

    protected TaskRouter doBuild() {
        if (paths == null || paths.isEmpty()) {
            return EmptyRouter.INSTANCE;
        }
        var map = new HashMap<Path, MethodMap>();
        var flags = new boolean[]{true, true};
        paths.forEach((path, builder) -> {
            var methodMap = builder.build();
            if (methodMap.empty()) {
                return;
            }
            map.put(path, methodMap);
            methodMap.forEach(task -> {
                flags[0] = flags[0] && task.isSync();
                flags[1] = flags[1] && task.isAsync();
            });
        });
        return new WrappedRouter(factory.create(map), flags[0], flags[1]);
    }

    public TaskRouter build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
