package io.github.amayaframework.routing;

import io.github.amayaframework.application.Resettable;

import java.util.function.Consumer;

public interface RoutingConfigurer extends Resettable {

    TaskRouter router();

    RoutingConfigurer router(TaskRouter router);

    RouterConfigurer routerConfigurer();

    RoutingConfigurer router(Consumer<RouterConfigurer> action);

    ParamParser paramParser();

    RoutingConfigurer paramParser(ParamParser parser);

    ParamParserConfigurer paramParserConfigurer();

    RoutingConfigurer paramParser(Consumer<ParamParserConfigurer> action);

    boolean handleOptionsRequest();

    RoutingConfigurer handleOptionsRequest(boolean handle);

    String cacheControl();

    RoutingConfigurer cacheControl(String cacheControl);
}
