package io.github.amayaframework.routing;

/**
 * TODO
 * @param <C>
 */
public abstract class AbstractRoutingConfigurer<C extends RoutingConfigurer> implements RoutingConfigurer {
    protected TaskRouter router;
    protected ParamParser parser;
    protected boolean handleOptionsRequest;
    protected String cacheControl;

    @Override
    public void reset() {
        this.router = null;
        this.parser = null;
        this.handleOptionsRequest = true;
        this.cacheControl = null;
    }

    @Override
    public TaskRouter router() {
        return router;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C router(TaskRouter router) {
        this.router = router;
        return (C) this;
    }

    @Override
    public ParamParser paramParser() {
        return parser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C paramParser(ParamParser parser) {
        this.parser = parser;
        return (C) this;
    }

    @Override
    public boolean handleOptionsRequest() {
        return handleOptionsRequest;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C handleOptionsRequest(boolean handle) {
        this.handleOptionsRequest = handle;
        return (C) this;
    }

    @Override
    public String cacheControl() {
        return cacheControl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C cacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
        return (C) this;
    }
}
