package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

/**
 *
 */
public class AsyncRoutingTask extends AbstractRoutingTask implements AsyncTaskConsumer<HttpContext> {

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     * @param cacheControl
     */
    public AsyncRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     */
    public AsyncRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    /**
     *
     * @param router
     * @param parser
     */
    public AsyncRoutingTask(Router<MethodMap> router, ParamParser parser) {
        super(router, parser, true, null);
    }
}
