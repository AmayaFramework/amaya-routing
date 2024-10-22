package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import com.github.romanqed.jfunc.Runnable2;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.router.RouterFactory;
import io.github.amayaframework.web.WebApplication;

import java.util.Objects;
import java.util.function.Supplier;

final class ParsingRoutingConfigurer implements RoutingConfigurer {
    private final RouterFactory factory;
    private final ParsingPathSet paths;
    private final Supplier<FilterSet> supplier;
    private FilterSet filters;

    ParsingRoutingConfigurer(RouterFactory factory, PathParser parser, Supplier<FilterSet> supplier) {
        this.factory = factory;
        this.paths = new ParsingPathSet(parser);
        this.supplier = supplier;
        this.filters = supplier.get();
    }

    @Override
    public PathSet getPathSet() {
        return paths;
    }

    @Override
    public FilterSet getFilterSet() {
        return filters;
    }

    private RoutingHandler create() {
        var router = factory.create(paths.map);
        return new RoutingHandler(router, filters);
    }

    @Override
    public Runnable2<HttpContext, Runnable1<HttpContext>> createHandler() {
        var ret = create();
        filters = supplier.get();
        paths.clear();
        return ret;
    }

    @Override
    public void run(WebApplication application) {
        Objects.requireNonNull(application);
        var handler = create();
        filters = supplier.get();
        paths.clear();
        application.addHandler(handler);
    }
}
