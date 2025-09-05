package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.filter.MapFilterSet;

/**
 * TODO
 */
public class ParamParserBuilder extends AbstractParamParserConfigurer<ParamParserBuilder> {

    @Override
    protected FilterSet createFilterSet() {
        return new MapFilterSet();
    }

    /**
     * TODO
     * @return
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
     * TODO
     * @return
     */
    public ParamParser build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
