package io.github.amayaframework.routing;

import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.router.RouterFactory;

/**
 * An interface describing an abstract {@link RoutingConfigurer} factory.
 */
public interface RoutingConfigurerFactory {

    /**
     * Creates a {@link RoutingConfigurer} with given {@link RouterFactory} and {@link PathParser}.
     *
     * @param factory the specified {@link RouterFactory}, must be non-null
     * @param parser  the specified {@link PathParser}, must be non-null
     * @return the {@link RoutingConfigurer} instance
     */
    RoutingConfigurer create(RouterFactory factory, PathParser parser);
}
