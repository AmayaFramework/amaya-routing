package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.router.Router;

public class AsyncRoutingTask extends AbstractRoutingTask implements AsyncTaskConsumer<HttpContext> {

    public AsyncRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions, String cacheControl) {
        super(router, filters, handleOptions, cacheControl);
    }

    public AsyncRoutingTask(Router<MethodMap> router, FilterSet filterSet, boolean handleOptions) {
        super(router, filterSet, handleOptions, null);
    }

    public AsyncRoutingTask(Router<MethodMap> router, FilterSet filterSet) {
        super(router, filterSet, true, null);
    }
}
