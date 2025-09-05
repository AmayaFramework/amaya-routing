package io.github.amayaframework.routing;

import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

/**
 *
 */
public class UniRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     * @param cacheControl
     */
    public UniRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    /**
     *
     * @param router
     * @param parser
     * @param handleOptions
     */
    public UniRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    /**
     *
     * @param router
     * @param parser
     */
    public UniRoutingTask(Router<MethodMap> router, ParamParser parser) {
        super(router, parser, true, null);
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
