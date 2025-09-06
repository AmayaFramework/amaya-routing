package io.github.amayaframework.routing;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.filter.FilterSet;

/**
 *
 */
public interface ParamParserConfigurer extends Resettable {

    /**
     *
     * @return
     */
    FilterSet filterSet();

    /**
     *
     * @param set
     * @return
     */
    ParamParserConfigurer filterSet(FilterSet set);

    /**
     *
     * @return
     */
    boolean decodePath();

    /**
     *
     * @param decode
     * @return
     */
    ParamParserConfigurer decodePath(boolean decode);

    /**
     *
     * @return
     */
    boolean decodeQuery();

    /**
     *
     * @param decode
     * @return
     */
    ParamParserConfigurer decodeQuery(boolean decode);

    /**
     *
     * @return
     */
    UrlDecoder decoder();

    /**
     *
     * @param decoder
     * @return
     */
    ParamParserConfigurer decoder(UrlDecoder decoder);
}
