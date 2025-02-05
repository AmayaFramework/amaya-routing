package io.github.amayaframework.routing;

import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;
import io.github.amayaframework.router.StaticRouterFactory;

/**
 * A utility class for creating instances of {@link RoutingConfigurer}.
 * This class provides static methods to create routing configurations
 * using different factories and parsers.
 */
public final class RoutingConfigurers {

    /**
     * The singleton factory used to create routing configurators.
     * This is an instance of {@link ParsingConfigurerFactory}.
     */
    public static final RoutingConfigurerFactory CONFIGURER_FACTORY = new ParsingConfigurerFactory();

    private RoutingConfigurers() {
    }

    /**
     * Creates a {@link RoutingConfigurer} using the specified router factory and path parser.
     *
     * @param factory the router factory to be used, must be non-null
     * @param parser  the path parser to be used, must be non-null
     * @return a new instance of {@link RoutingConfigurer}
     */
    public static RoutingConfigurer create(RouterFactory factory, PathParser parser) {
        return CONFIGURER_FACTORY.create(factory, parser);
    }

    /**
     * Creates a {@link RoutingConfigurer} using the specified path parser.
     * A default router factory is created using a new instance of {@link StaticRouterFactory}.
     *
     * @param parser the path parser to be used, must be non-null
     * @return a new instance of {@link RoutingConfigurer}
     */
    public static RoutingConfigurer create(PathParser parser) {
        return CONFIGURER_FACTORY.create(new StaticRouterFactory(), parser);
    }

    /**
     * Creates a {@link RoutingConfigurer} using a default path parser.
     *
     * @param factory the router factory to be used, must be non-null
     * @return a new instance of {@link RoutingConfigurer}
     */
    public static RoutingConfigurer create(RouterFactory factory) {
        return CONFIGURER_FACTORY.create(factory, PathParsers.createDefault());
    }

    /**
     * Creates a {@link RoutingConfigurer} using a default path parser.
     * A default router factory is created using a new instance of {@link StaticRouterFactory}.
     *
     * @return a new instance of {@link RoutingConfigurer}
     */
    public static RoutingConfigurer create() {
        return CONFIGURER_FACTORY.create(new StaticRouterFactory(), PathParsers.createDefault());
    }
}
