package io.github.amayaframework.routing;

import io.github.amayaframework.path.parser.PathParser;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Routing {
    private Routing() {
    }

    public static RouteBuilder routeBuilder(Supplier<MethodMap> supplier, boolean preferExtended) {
        Objects.requireNonNull(supplier);
        return new RouteBuilder(supplier, preferExtended);
    }

    public static RouteBuilder routeBuilder(Supplier<MethodMap> supplier) {
        Objects.requireNonNull(supplier);
        return new RouteBuilder(supplier, false);
    }

    public static RouteBuilder routeBuilder() {
        return new RouteBuilder(IdentityMethodMap::new, false);
    }

    public static RouterBuilder routerBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(parser, factory, supplier);
    }

    public static RouterBuilder routerBuilder(PathParser parser, RouterFactory factory) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        return new RouterBuilder(parser, factory, RouteBuilder::new);
    }

    public static RouterBuilder routerBuilder(PathParser parser, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(
                parser,
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG"),
                supplier
        );
    }

    public static RouterBuilder routerBuilder(RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(PathParsers.createDefault(), factory, supplier);
    }

    public static RouterBuilder routerBuilder(PathParser parser) {
        Objects.requireNonNull(parser);
        return new RouterBuilder(
                parser,
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG"),
                RouteBuilder::new
        );
    }

    public static RouterBuilder routerBuilder(RouterFactory factory) {
        Objects.requireNonNull(factory);
        return new RouterBuilder(
                PathParsers.createDefault(),
                factory,
                RouteBuilder::new
        );
    }

    public static RouterBuilder routerBuilder(Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(supplier);
        return new RouterBuilder(
                PathParsers.createDefault(),
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG"),
                supplier
        );
    }

    public static RouterBuilder routerBuilder() {
        return new RouterBuilder(
                PathParsers.createDefault(),
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG"),
                RouteBuilder::new
        );
    }

    public static RoutingBuilder routingBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    public static RoutingBuilder routingBuilder(PathParser parser, RouterFactory factory) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    public static RoutingBuilder routingBuilder(PathParser parser, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(supplier);
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    public static RoutingBuilder routingBuilder(RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        var parser = PathParsers.createDefault();
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    public static RoutingBuilder routingBuilder(PathParser parser) {
        Objects.requireNonNull(parser);
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    public static RoutingBuilder routingBuilder(RouterFactory factory) {
        Objects.requireNonNull(factory);
        var parser = PathParsers.createDefault();
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    public static RoutingBuilder routingBuilder(Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(supplier);
        var parser = PathParsers.createDefault();
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    public static RoutingBuilder routingBuilder() {
        var parser = PathParsers.createDefault();
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "TODO MSG");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    public static RoutingApplicationConfigurer configurer(RoutingBuilder builder, boolean configure) {
        return new RoutingApplicationConfigurer(builder, configure);
    }

    public static RoutingApplicationConfigurer configurer(boolean configure) {
        return new RoutingApplicationConfigurer(routingBuilder(), configure);
    }

    public static RoutingApplicationConfigurer configurer(RoutingBuilder builder, Consumer<RoutingConfigurer> action) {
        var ret = new RoutingApplicationConfigurer(builder, false);
        action.accept(ret.getConfigurer());
        return ret;
    }

    public static RoutingApplicationConfigurer configurer(Consumer<RoutingConfigurer> action) {
        var ret = new RoutingApplicationConfigurer(routingBuilder(), false);
        action.accept(ret.getConfigurer());
        return ret;
    }

    public static RoutingApplicationConfigurer configurer() {
        return new RoutingApplicationConfigurer(routingBuilder(), false);
    }
}
