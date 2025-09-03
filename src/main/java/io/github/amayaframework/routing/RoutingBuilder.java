package io.github.amayaframework.routing;

import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoutingBuilder extends AbstractRoutingConfigurer<RoutingBuilder> {
    protected final Supplier<RouterBuilder> supplier;
    protected RouterBuilder routerBuilder;
    protected ParamParserBuilder paramParserBuilder;

    public RoutingBuilder(Supplier<RouterBuilder> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void reset() {
        super.reset();
        routerBuilder = null;
        paramParserBuilder = null;
    }

    protected void ensureRouterBuilder() {
        if (routerBuilder == null) {
            routerBuilder = supplier.get();
        }
    }

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

    protected TaskConsumer<HttpContext> doBuild() {
        var router = buildRouter();
        if (router.empty()) {
            return new NotFoundTask();
        }
        var parser = buildParser();
        // Лучший случай (uni = true)
        if (router.isUni()) {
            return new UniRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Не умеет uni, но умеет sync (sync = true, async = false, uni = false)
        if (router.isSync()) {
            return new SyncRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Не умеет uni, но умеет async (sync = false, async = true, uni = false)
        if (router.isAsync()) {
            return new AsyncRoutingTask(router, parser, handleOptionsRequest, cacheControl);
        }
        // Худший случай - mixed
        return new MixedRoutingTask(router, parser, handleOptionsRequest, cacheControl);
    }

    public TaskConsumer<HttpContext> build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
