package io.github.amayaframework.routing;

import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.router.Router;

public class UniRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions, String cacheControl) {
        super(router, filters, handleOptions, cacheControl);
    }

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters, boolean handleOptions) {
        super(router, filters, handleOptions, null);
    }

    public UniRoutingTask(Router<MethodMap> router, FilterSet filters) {
        super(router, filters, true, null);
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean isUni() {
        return true;
    }
}
