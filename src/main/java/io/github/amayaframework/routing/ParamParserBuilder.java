package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.filter.MapFilterSet;

/**
 * A builder for creating {@link ParamParser} instances.
 * <p>
 * This builder extends {@link AbstractParamParserConfigurer}, allowing
 * fluent configuration of filters, decoding options, and decoders.
 * <p>
 * The resulting parser is usually a {@link FilterParamParser}, unless no
 * configuration is provided, in which case {@link #build()} returns {@code null}.
 */
public class ParamParserBuilder extends AbstractParamParserConfigurer<ParamParserBuilder> {

    @Override
    protected FilterSet createFilterSet() {
        return new MapFilterSet();
    }

    /**
     * Creates a {@link ParamParser} using the current configuration.
     * <p>
     * Returns {@code null} if no filters are defined and both path and query
     * decoding are disabled, meaning that parameter parsing is not needed.
     *
     * @return a configured {@link ParamParser}, or {@code null} if no parsing is required
     */
    protected ParamParser doBuild() {
        if ((filters == null || filters.empty()) && !decodePath && !decodeQuery) {
            return null;
        }
        var filters = this.filters == null ? createFilterSet() : this.filters;
        var decoder = (decodePath || decodeQuery) && this.decoder == null ? new StandardUrlDecoder() : this.decoder;
        return new FilterParamParser(filters, decoder, decodePath, decodeQuery);
    }

    /**
     * Builds a {@link ParamParser} and resets this builder.
     * <p>
     * This method is safe to call multiple times; after each call the builder
     * is cleared and may be reused with new configuration.
     *
     * @return a configured {@link ParamParser}, or {@code null} if no parsing is required
     */
    public ParamParser build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
