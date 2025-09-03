package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.filter.MapFilterSet;

public class ParamParserBuilder extends AbstractParamParserConfigurer<ParamParserBuilder> {

    @Override
    protected FilterSet createFilterSet() {
        return new MapFilterSet();
    }

    protected ParamParser doBuild() {
        if ((filters == null || filters.empty()) && !decodePath && !decodeQuery) {
            return null;
        }
        var filters = this.filters == null ? createFilterSet() : this.filters;
        var decoder = (decodePath || decodeQuery) && this.decoder == null ? new StandardUrlDecoder() : this.decoder;
        return new FilterParamParser(filters, decoder, decodePath, decodeQuery);
    }

    public ParamParser build() {
        try {
            return doBuild();
        } finally {
            reset();
        }
    }
}
