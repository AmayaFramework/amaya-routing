package io.github.amayaframework.routing;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.parser.PathParser;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A builder for constructing {@link TaskRouter} instances.
 * <p>
 * This builder extends {@link AbstractRouterConfigurer} and assembles
 * a collection of {@link RouteBuilder route builders} mapped by {@link Path}.
 * Each route produces a {@link MethodMap}, which is then combined into
 * a router created by the provided {@link RouterFactory}.
 * <p>
 * The builder also determines whether the resulting router can be executed
 * synchronously and/or asynchronously by inspecting the tasks it contains.
 * <p>
 * If no routes are defined, the builder produces {@link EmptyRouter#INSTANCE}.
 */
public class RouterBuilder extends AbstractRouterConfigurer<RouterBuilder, RouteBuilder> {
    protected final RouterFactory factory;
    protected final Supplier<RouteBuilder> supplier;

    /**
     * Creates a new router builder with custom path parser, router factory, and route builder supplier.
     *
     * @param parser   the {@link PathParser} used to parse string path patterns
     * @param factory  the factory used to construct the underlying router
     * @param supplier a supplier of {@link RouteBuilder} instances for new paths
     */
    public RouterBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        super(parser);
        this.factory = factory;
        this.supplier = supplier;
    }

    /**
     * Creates a new router builder with a custom parser and factory,
     * using {@link RouteBuilder#RouteBuilder()} as the default route builder supplier.
     *
     * @param parser  the {@link PathParser} used to parse string path patterns
     * @param factory the factory used to construct the underlying router
     */
    public RouterBuilder(PathParser parser, RouterFactory factory) {
        this(parser, factory, RouteBuilder::new);
    }

    /**
     * Creates a new router builder with a default path parser
     * ({@link PathParsers#createDefault()}) and a custom factory.
     *
     * @param factory the factory used to construct the underlying router
     */
    public RouterBuilder(RouterFactory factory) {
        this(PathParsers.createDefault(), factory);
    }

    @Override
    protected RouteBuilder createRouteConfigurer() {
        return supplier.get();
    }

    /**
     * Builds a {@link TaskRouter} without resetting the builder state.
     * <p>
     * If no routes are defined, returns {@link EmptyRouter#INSTANCE}.
     * Otherwise, creates a router from all accumulated paths and methods.
     * <p>
     * The router is wrapped in a {@link WrappedRouter} that records whether
     * all tasks are synchronous and/or asynchronous for efficient execution.
     *
     * @return a task router for the configured routes
     */
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

    /**
     * Builds a {@link TaskRouter} and resets this builder.
     * <p>
     * After calling this method, the builder state is cleared
     * and it may be reused for new configurations.
     *
     * @return the constructed router
     */
    public TaskRouter build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
