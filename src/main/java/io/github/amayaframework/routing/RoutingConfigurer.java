package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import com.github.romanqed.jfunc.Runnable2;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.web.WebApplication;

/**
 *
 */
public interface RoutingConfigurer extends Runnable1<WebApplication> {

    /**
     *
     * @return
     */
    PathSet getPathSet();

    /**
     *
     * @return
     */
    FilterSet getFilterSet();

    /**
     *
     * @return
     */
    Runnable2<HttpContext, Runnable1<HttpContext>> createHandler();

    /**
     *
     * @param application function parameter
     * @throws Throwable
     */
    @Override
    void run(WebApplication application) throws Throwable;
}
