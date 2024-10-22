package io.github.amayaframework.routing;

import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.router.RouterFactory;

/**
 *
 */
public interface RoutingConfigurerFactory {

    /**
     * @param factory
     * @param parser
     * @return
     */
    RoutingConfigurer create(RouterFactory factory, PathParser parser);
}
