package io.github.amayaframework.routing;

import io.github.amayaframework.path.parser.PathParser;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Static factory methods for constructing routing-related builders and configurers.
 * <p>
 * Provides convenient entry points for creating:
 * <ul>
 *   <li>{@link RouteBuilder} – for configuring routes for a single path.</li>
 *   <li>{@link RouterBuilder} – for assembling multiple paths into a router.</li>
 *   <li>{@link RoutingBuilder} – for building a full routing pipeline.</li>
 *   <li>{@link RoutingApplicationConfigurer} – for integrating routing with a web application.</li>
 * </ul>
 * <p>
 * Most methods have multiple overloads to allow custom {@link PathParser},
 * {@link RouterFactory}, or builder suppliers.
 */
public final class Routing {
    private Routing() {
    }

    /**
     * Creates a {@link RouteBuilder} with a custom method map supplier and strategy.
     *
     * @param supplier       the supplier for extended {@link MethodMap} implementations
     * @param preferExtended whether extended maps should be preferred over the default {@link ArrayMethodMap}
     * @return a new route builder
     */
    public static RouteBuilder routeBuilder(Supplier<MethodMap> supplier, boolean preferExtended) {
        Objects.requireNonNull(supplier);
        return new RouteBuilder(supplier, preferExtended);
    }

    /**
     * Creates a {@link RouteBuilder} with a custom method map supplier.
     *
     * @param supplier the supplier for extended {@link MethodMap} implementations
     * @return a new route builder
     */
    public static RouteBuilder routeBuilder(Supplier<MethodMap> supplier) {
        Objects.requireNonNull(supplier);
        return new RouteBuilder(supplier, false);
    }

    /**
     * Creates a {@link RouteBuilder} with default settings
     * using {@link IdentityMethodMap} as the extended map supplier.
     *
     * @return a new route builder
     */
    public static RouteBuilder routeBuilder() {
        return new RouteBuilder(IdentityMethodMap::new, false);
    }

    /**
     * Creates a {@link RouterBuilder} with custom parser, factory, and route builder supplier.
     *
     * @param parser   the parser for path templates
     * @param factory  the factory for constructing router instances
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new router builder
     */
    public static RouterBuilder routerBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(parser, factory, supplier);
    }

    /**
     * Creates a {@link RouterBuilder} with custom parser and factory.
     *
     * @param parser  the parser for path templates
     * @param factory the factory for constructing router instances
     * @return a new router builder
     */
    public static RouterBuilder routerBuilder(PathParser parser, RouterFactory factory) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        return new RouterBuilder(parser, factory, RouteBuilder::new);
    }

    /**
     * Creates a {@link RouterBuilder} with custom parser and route builder supplier.
     * The router factory is discovered via {@link LookupUtil#lookupRouterFactory()}.
     *
     * @param parser   the parser for path templates
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new router builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RouterBuilder routerBuilder(PathParser parser, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(
                parser,
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed"),
                supplier
        );
    }

    /**
     * Creates a {@link RouterBuilder} with custom factory and route builder supplier,
     * using the default path parser.
     *
     * @param factory  the factory for constructing router instances
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new router builder
     */
    public static RouterBuilder routerBuilder(RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RouterBuilder(PathParsers.createDefault(), factory, supplier);
    }

    /**
     * Creates a {@link RouterBuilder} with custom parser.
     * The router factory is discovered via {@link LookupUtil#lookupRouterFactory()}.
     *
     * @param parser the parser for path templates
     * @return a new router builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RouterBuilder routerBuilder(PathParser parser) {
        Objects.requireNonNull(parser);
        return new RouterBuilder(
                parser,
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed"),
                RouteBuilder::new
        );
    }

    /**
     * Creates a {@link RouterBuilder} with custom factory and default path parser.
     *
     * @param factory the factory for constructing router instances
     * @return a new router builder
     */
    public static RouterBuilder routerBuilder(RouterFactory factory) {
        Objects.requireNonNull(factory);
        return new RouterBuilder(
                PathParsers.createDefault(),
                factory,
                RouteBuilder::new
        );
    }

    /**
     * Creates a {@link RouterBuilder} with a custom route builder supplier.
     * The router factory is discovered via {@link LookupUtil#lookupRouterFactory()}.
     *
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new router builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RouterBuilder routerBuilder(Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(supplier);
        return new RouterBuilder(
                PathParsers.createDefault(),
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed"),
                supplier
        );
    }

    /**
     * Creates a {@link RouterBuilder} with default path parser and factory discovered via SPI.
     *
     * @return a new router builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RouterBuilder routerBuilder() {
        return new RouterBuilder(
                PathParsers.createDefault(),
                Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed"),
                RouteBuilder::new
        );
    }

    /**
     * Creates a {@link RoutingBuilder} with custom parser, factory, and route builder supplier.
     *
     * @param parser   the parser for path templates
     * @param factory  the factory for constructing router instances
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new routing builder
     */
    public static RoutingBuilder routingBuilder(PathParser parser, RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    /**
     * Creates a {@link RoutingBuilder} with custom parser and factory.
     *
     * @param parser  the parser for path templates
     * @param factory the factory for constructing router instances
     * @return a new routing builder
     */
    public static RoutingBuilder routingBuilder(PathParser parser, RouterFactory factory) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(factory);
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    /**
     * Creates a {@link RoutingBuilder} with custom parser and route builder supplier.
     * The router factory is discovered via {@link LookupUtil#lookupRouterFactory()}.
     *
     * @param parser   the parser for path templates
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new routing builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RoutingBuilder routingBuilder(PathParser parser, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(parser);
        Objects.requireNonNull(supplier);
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    /**
     * Creates a {@link RoutingBuilder} with custom factory and route builder supplier,
     * using the default path parser.
     *
     * @param factory  the factory for constructing router instances
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new routing builder
     */
    public static RoutingBuilder routingBuilder(RouterFactory factory, Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(supplier);
        var parser = PathParsers.createDefault();
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    /**
     * Creates a {@link RoutingBuilder} with custom parser.
     * The router factory is discovered via {@link LookupUtil#lookupRouterFactory()}.
     *
     * @param parser the parser for path templates
     * @return a new routing builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RoutingBuilder routingBuilder(PathParser parser) {
        Objects.requireNonNull(parser);
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    /**
     * Creates a {@link RoutingBuilder} with custom factory and default path parser.
     *
     * @param factory the factory for constructing router instances
     * @return a new routing builder
     */
    public static RoutingBuilder routingBuilder(RouterFactory factory) {
        Objects.requireNonNull(factory);
        var parser = PathParsers.createDefault();
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    /**
     * Creates a {@link RoutingBuilder} with a custom route builder supplier
     * and default path parser. The router factory is discovered via SPI.
     *
     * @param supplier the supplier of {@link RouteBuilder} instances
     * @return a new routing builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RoutingBuilder routingBuilder(Supplier<RouteBuilder> supplier) {
        Objects.requireNonNull(supplier);
        var parser = PathParsers.createDefault();
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, supplier));
    }

    /**
     * Creates a {@link RoutingBuilder} with default path parser and factory discovered via SPI.
     *
     * @return a new routing builder
     * @throws NullPointerException if no {@link RouterFactory} can be discovered
     */
    public static RoutingBuilder routingBuilder() {
        var parser = PathParsers.createDefault();
        var factory = Objects.requireNonNull(LookupUtil.lookupRouterFactory(), "RouterFactory lookup failed");
        return new RoutingBuilder(() -> new RouterBuilder(parser, factory, RouteBuilder::new));
    }

    /**
     * Creates a {@link RoutingApplicationConfigurer} with the given builder.
     *
     * @param builder   the routing builder
     * @param configure whether to apply {@link RoutingOptions} automatically
     * @return a new application configurator
     */
    public static RoutingApplicationConfigurer configurer(RoutingBuilder builder, boolean configure) {
        return new RoutingApplicationConfigurer(builder, configure);
    }

    /**
     * Creates a {@link RoutingApplicationConfigurer} with default builder.
     *
     * @param configure whether to apply {@link RoutingOptions} automatically
     * @return a new application configurator
     */
    public static RoutingApplicationConfigurer configurer(boolean configure) {
        return new RoutingApplicationConfigurer(routingBuilder(), configure);
    }

    /**
     * Creates a {@link RoutingApplicationConfigurer} with a given builder and inline configuration.
     *
     * @param builder the routing builder
     * @param action  a consumer for configuring the routing builder
     * @return a new application configurator
     */
    public static RoutingApplicationConfigurer configurer(RoutingBuilder builder, Consumer<RoutingConfigurer> action) {
        var ret = new RoutingApplicationConfigurer(builder, false);
        action.accept(ret.getConfigurer());
        return ret;
    }

    /**
     * Creates a {@link RoutingApplicationConfigurer} with default builder and inline configuration.
     *
     * @param action a consumer for configuring the routing builder
     * @return a new application configurator
     */
    public static RoutingApplicationConfigurer configurer(Consumer<RoutingConfigurer> action) {
        var ret = new RoutingApplicationConfigurer(routingBuilder(), false);
        action.accept(ret.getConfigurer());
        return ret;
    }

    /**
     * Creates a {@link RoutingApplicationConfigurer} with default builder and no auto-configuration.
     *
     * @return a new application configurator
     */
    public static RoutingApplicationConfigurer configurer() {
        return new RoutingApplicationConfigurer(routingBuilder(), false);
    }
}
