package io.github.amayaframework.routing;

import com.github.romanqed.jconv.SyncTaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.router.Router;

public class SyncRoutingTask extends AbstractRoutingTask implements SyncTaskConsumer<HttpContext> {

    public SyncRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions, String cacheControl) {
        super(router, filters, handleOptions, cacheControl);
    }

    public SyncRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions) {
        super(router, filters, handleOptions, null);
    }

    public SyncRoutingTask(Router<MethodMap> router, FilterSet filters) {
        super(router, filters, true, null);
    }
}
