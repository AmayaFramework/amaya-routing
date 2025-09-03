package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;

public abstract class AbstractParamParserConfigurer<C extends ParamParserConfigurer> implements ParamParserConfigurer {
    protected FilterSet filters;
    protected boolean decodePath;
    protected boolean decodeQuery;
    protected UrlDecoder decoder;

    @Override
    public void reset() {
        this.filters = null;
        this.decodePath = false;
        this.decodeQuery = false;
        this.decoder = null;
    }

    protected abstract FilterSet createFilterSet();

    @Override
    public FilterSet filterSet() {
        if (filters == null) {
            filters = createFilterSet();
        }
        return filters;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C filterSet(FilterSet set) {
        filters = set;
        return (C) this;
    }

    @Override
    public boolean decodePath() {
        return decodePath;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParamParserConfigurer decodePath(boolean decode) {
        this.decodePath = decode;
        return this;
    }

    @Override
    public boolean decodeQuery() {
        return decodeQuery;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParamParserConfigurer decodeQuery(boolean decode) {
        this.decodeQuery = decode;
        return this;
    }

    @Override
    public UrlDecoder decoder() {
        return decoder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParamParserConfigurer decoder(UrlDecoder decoder) {
        this.decoder = decoder;
        return this;
    }
}
