package io.github.amayaframework.routing;

import com.github.romanqed.jconv.TaskConsumer;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.router.Router;

/**
 * A unified routing task implementation.
 * <p>
 * This class extends {@link AbstractRoutingTask} and implements {@link TaskConsumer},
 * marking itself as capable of both synchronous and asynchronous execution.
 * <p>
 * It integrates with a {@link Router} to resolve routes and dispatch requests
 * to the appropriate handlers, while also handling standard HTTP semantics such as
 * 404 (Not Found), 405 (Method Not Allowed), and automatic {@code OPTIONS} responses.
 * <p>
 * Unlike {@link SyncRoutingTask} or {@link AsyncRoutingTask}, this task reports
 * itself as {@link #isSync()}, {@link #isAsync()}, and {@link #isUni()} all returning {@code true}.
 */
public class UniRoutingTask extends AbstractRoutingTask implements TaskConsumer<HttpContext> {

    /**
     * Creates a new unified routing task.
     *
     * @param router        the underlying router used to resolve paths
     * @param parser        the parameter parser to apply (may be {@code null})
     * @param handleOptions whether {@code OPTIONS} requests should be handled automatically
     * @param cacheControl  optional value for the {@code Cache-Control} response header (may be {@code null})
     */
    public UniRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions, String cacheControl) {
        super(router, parser, handleOptions, cacheControl);
    }

    /**
     * Creates a new unified routing task without a custom {@code Cache-Control} header.
     *
     * @param router        the underlying router used to resolve paths
     * @param parser        the parameter parser to apply (may be {@code null})
     * @param handleOptions whether {@code OPTIONS} requests should be handled automatically
     */
    public UniRoutingTask(Router<MethodMap> router, ParamParser parser, boolean handleOptions) {
        super(router, parser, handleOptions, null);
    }

    /**
     * Creates a new unified routing task with default options:
     * <ul>
     *     <li>{@code handleOptions} is set to {@code true}</li>
     *     <li>{@code cacheControl} is {@code null}</li>
     * </ul>
     *
     * @param router the underlying router used to resolve paths
     * @param parser the parameter parser to apply (may be {@code null})
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
