package io.github.amayaframework.routing;

import com.github.romanqed.jconv.SyncTaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

public class SyncRoutingTask extends AbstractRoutingTask implements SyncTaskConsumer<HttpContext> {

    public SyncRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    public SyncRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    public SyncRoutingTask(Router<MethodMap> router, ParamParser parser) {
        super(router, parser, true, null);
    }
}
