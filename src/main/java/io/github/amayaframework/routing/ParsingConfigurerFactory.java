package io.github.amayaframework.routing;

import io.github.amayaframework.filter.MapFilterSet;
import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.router.RouterFactory;

import java.util.Objects;

/**
 * Implementation of {@link RoutingConfigurerFactory} that creates basic routing configurer.
 */
public final class ParsingConfigurerFactory implements RoutingConfigurerFactory {

    @Override
    public RoutingConfigurer create(RouterFactory factory, PathParser parser) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(parser);
        return new ParsingRoutingConfigurer(factory, parser, MapFilterSet::new);
    }
}
