package io.github.amayaframework.routing;

import io.github.amayaframework.application.Resettable;

import java.util.function.Consumer;

/**
 * Defines a fluent interface for configuring the overall routing system.
 * <p>
 * This configurer ties together the {@link TaskRouter}, the {@link RouterConfigurer}
 * for defining routes, and the {@link ParamParserConfigurer} for handling
 * path and query parameters. It also provides options for handling
 * {@code OPTIONS} requests automatically and setting default caching headers.
 */
public interface RoutingConfigurer extends Resettable {

    /**
     * Returns the currently configured task router.
     *
     * @return the active {@link TaskRouter}
     */
    TaskRouter router();

    /**
     * Sets the task router to use.
     *
     * @param router the new {@link TaskRouter}
     * @return this configurer
     */
    RoutingConfigurer router(TaskRouter router);

    /**
     * Returns the {@link RouterConfigurer} for configuring routes.
     *
     * @return the router configurer
     */
    RouterConfigurer routerConfigurer();

    /**
     * Configures routes using the provided action.
     *
     * @param action a consumer that configures the router
     * @return this configurer
     */
    RoutingConfigurer router(Consumer<RouterConfigurer> action);

    /**
     * Returns the currently configured parameter parser.
     *
     * @return the active {@link ParamParser}
     */
    ParamParser paramParser();

    /**
     * Sets the parameter parser to use.
     *
     * @param parser the new {@link ParamParser}
     * @return this configurer
     */
    RoutingConfigurer paramParser(ParamParser parser);

    /**
     * Returns the {@link ParamParserConfigurer} for fine-grained parameter parsing settings.
     *
     * @return the parameter parser configurer
     */
    ParamParserConfigurer paramParserConfigurer();

    /**
     * Configures the parameter parser using the provided action.
     *
     * @param action a consumer that configures the parser
     * @return this configurer
     */
    RoutingConfigurer paramParser(Consumer<ParamParserConfigurer> action);

    /**
     * Indicates whether {@code OPTIONS} requests should be handled automatically.
     *
     * @return {@code true} if automatic handling is enabled, {@code false} otherwise
     */
    boolean handleOptionsRequest();

    /**
     * Enables or disables automatic handling of {@code OPTIONS} requests.
     *
     * @param handle whether to handle options requests automatically
     * @return this configurer
     */
    RoutingConfigurer handleOptionsRequest(boolean handle);

    /**
     * Returns the default value of the {@code Cache-Control} header.
     *
     * @return the cache control header value, or {@code null} if not set
     */
    String cacheControl();

    /**
     * Sets the default value of the {@code Cache-Control} header.
     *
     * @param cacheControl the header value to use (may be {@code null})
     * @return this configurer
     */
    RoutingConfigurer cacheControl(String cacheControl);
}
