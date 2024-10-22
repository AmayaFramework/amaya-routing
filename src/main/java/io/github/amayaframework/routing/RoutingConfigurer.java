package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import com.github.romanqed.jfunc.Runnable2;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.web.WebApplication;

/**
 * An interface describing an abstract routing configurator.
 * It can be applied to {@link WebApplication} via
 * {@link io.github.amayaframework.web.WebApplicationBuilder#configureApplication(Runnable1)}
 * or {@link WebApplication#addHandler(Runnable2)}.
 */
public interface RoutingConfigurer extends Runnable1<WebApplication> {

    /**
     * Gets current http path set.
     *
     * @return the {@link PathSet} instance
     */
    PathSet getPathSet();

    /**
     * Gets current filter set.
     *
     * @return the {@link FilterSet} instance
     */
    FilterSet getFilterSet();

    /**
     * Creates http context handler and resets inner state of this configurer.
     *
     * @return the handler instance
     */
    Runnable2<HttpContext, Runnable1<HttpContext>> createHandler();

    /**
     * Adds to application context handler and resets inner state of this configurer.
     *
     * @param application the specified web application to be configured.
     * @throws Throwable if any problems occurred
     */
    @Override
    void run(WebApplication application) throws Throwable;
}
