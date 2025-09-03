package io.github.amayaframework.routing;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.filter.FilterSet;

public interface ParamParserConfigurer extends Resettable {
    FilterSet filterSet();

    ParamParserConfigurer filterSet(FilterSet set);

    boolean decodePath();

    ParamParserConfigurer decodePath(boolean decode);

    boolean decodeQuery();

    ParamParserConfigurer decodeQuery(boolean decode);

    UrlDecoder decoder();

    ParamParserConfigurer decoder(UrlDecoder decoder);
}
