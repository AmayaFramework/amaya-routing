package io.github.amayaframework.routing;

import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A builder for creating a complete routing task pipeline.
 * <p>
 * This builder combines a {@link RouterBuilder} for route definitions
 * and a {@link ParamParserBuilder} for parameter parsing, and produces
 * a {@link TaskConsumer} that can be used as middleware in the request pipeline.
 * <p>
 * The resulting consumer is chosen dynamically:
 * <ul>
 *   <li>{@link NotFoundTask} if no routes are defined.</li>
 *   <li>{@link UniRoutingTask} if the router supports unified execution.</li>
 *   <li>{@link SyncRoutingTask} if only synchronous execution is supported.</li>
 *   <li>{@link AsyncRoutingTask} if only asynchronous execution is supported.</li>
 *   <li>{@link MixedRoutingTask} as a fallback if task capabilities are mixed.</li>
 * </ul>
 */
public class RoutingBuilder extends AbstractRoutingConfigurer<RoutingBuilder> {
    protected final Supplier<RouterBuilder> supplier;
    protected RouterBuilder routerBuilder;
    protected ParamParserBuilder paramParserBuilder;

    /**
     * Creates a new routing builder with a given supplier of {@link RouterBuilder}.
     *
     * @param supplier a supplier for new {@link RouterBuilder} instances
     */
    public RoutingBuilder(Supplier<RouterBuilder> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void reset() {
        super.reset();
        routerBuilder = null;
        paramParserBuilder = null;
    }

    /**
     * Ensures that a {@link RouterBuilder} is available, creating one if necessary.
     */
    protected void ensureRouterBuilder() {
        if (routerBuilder == null) {
            routerBuilder = supplier.get();
        }
    }

    /**
     * Ensures that a {@link ParamParserBuilder} is available, creating one if necessary.
     */
    protected void ensureParamParserBuilder() {
        if (paramParserBuilder == null) {
            paramParserBuilder = new ParamParserBuilder();
        }
    }

    @Override
    public RouterConfigurer routerConfigurer() {
        ensureRouterBuilder();
        return routerBuilder;
    }

    @Override
    public RoutingBuilder router(Consumer<RouterConfigurer> action) {
        Objects.requireNonNull(action);
        ensureRouterBuilder();
        action.accept(routerBuilder);
        return this;
    }

    @Override
    public ParamParserConfigurer paramParserConfigurer() {
        ensureParamParserBuilder();
        return paramParserBuilder;
    }

    @Override
    public RoutingBuilder paramParser(Consumer<ParamParserConfigurer> action) {
        Objects.requireNonNull(action);
        ensureParamParserBuilder();
        action.accept(paramParserBuilder);
        return this;
    }

    private TaskRouter buildRouter() {
        if (router != null) {
            return router;
        }
        if (routerBuilder != null) {
            return routerBuilder.build();
        }
        return EmptyRouter.INSTANCE;
    }

    private ParamParser buildParser() {
        if (parser != null) {
            return parser;
        }
        if (paramParserBuilder != null) {
            return paramParserBuilder.build();
        }
        return null;
    }

    /**
     * Builds the routing task without resetting this builder.
     * <p>
     * Selects the most suitable routing task implementation depending
     * on the capabilities of the configured {@link TaskRouter}.
     *
     * @return a {@link TaskConsumer} representing the routing task
     */
    protected TaskConsumer<HttpContext> doBuild() {
        var router = buildRouter();
        if (router.empty()) {
            return new NotFoundTask();
        }
        var parser = buildParser();
        // Best case: the router supports both sync and async execution (uni = true)
        if (router.isUni()) {
            return new UniRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Router does not support uni, but supports sync only (sync = true, async = false, uni = false)
        if (router.isSync()) {
            return new SyncRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Router does not support uni, but supports async only (sync = false, async = true, uni = false)
        if (router.isAsync()) {
            return new AsyncRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Worst case: router contains a mix of sync-only and async-only tasks
        return new MixedRoutingTask(router, parser, handleOptionsRequest, cacheControl);
    }

    /**
     * Builds the routing task and resets this builder.
     *
     * @return the constructed {@link TaskConsumer}
     */
    public TaskConsumer<HttpContext> build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
