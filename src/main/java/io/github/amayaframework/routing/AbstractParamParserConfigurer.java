package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;

/**
 * A skeletal implementation of {@link ParamParserConfigurer}.
 * <p>
 * This class manages the common state for parameter parsing configuration,
 * including filters, URL decoding flags, and a {@link UrlDecoder}.
 * <p>
 * Subclasses must implement {@link #createFilterSet()} to provide the default
 * filter set instance.
 *
 * @param <C> the concrete self-type, allowing fluent API chaining
 */
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

    /**
     * Creates the default filter set used when none has been explicitly configured.
     *
     * @return a new filter set
     */
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
    public C decodePath(boolean decode) {
        this.decodePath = decode;
        return (C) this;
    }

    @Override
    public boolean decodeQuery() {
        return decodeQuery;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C decodeQuery(boolean decode) {
        this.decodeQuery = decode;
        return (C) this;
    }

    @Override
    public UrlDecoder decoder() {
        return decoder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C decoder(UrlDecoder decoder) {
        this.decoder = decoder;
        return (C) this;
    }
}
