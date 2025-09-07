package io.github.amayaframework.routing;

import io.github.amayaframework.application.Resettable;
import io.github.amayaframework.filter.FilterSet;

/**
 * Defines a fluent interface for configuring a {@link ParamParser}.
 * <p>
 * This configurer controls how request path and query parameters are decoded
 * and filtered before being applied to the {@link io.github.amayaframework.context.HttpContext}.
 * It also allows customization of the underlying {@link UrlDecoder}.
 */
public interface ParamParserConfigurer extends Resettable {

    /**
     * Returns the currently configured filter set.
     *
     * @return the active {@link FilterSet}
     */
    FilterSet filterSet();

    /**
     * Sets the filter set used for parameter processing.
     *
     * @param set the new {@link FilterSet}
     * @return this configurer
     */
    ParamParserConfigurer filterSet(FilterSet set);

    /**
     * Indicates whether path parameters should be URL-decoded.
     *
     * @return {@code true} if path parameters are decoded, {@code false} otherwise
     */
    boolean decodePath();

    /**
     * Enables or disables URL-decoding of path parameters.
     *
     * @param decode whether to decode path parameters
     * @return this configurer
     */
    ParamParserConfigurer decodePath(boolean decode);

    /**
     * Indicates whether query parameters should be URL-decoded.
     *
     * @return {@code true} if query parameters are decoded, {@code false} otherwise
     */
    boolean decodeQuery();

    /**
     * Enables or disables URL-decoding of query parameters.
     *
     * @param decode whether to decode query parameters
     * @return this configurer
     */
    ParamParserConfigurer decodeQuery(boolean decode);

    /**
     * Returns the {@link UrlDecoder} used for decoding parameters.
     *
     * @return the active decoder
     */
    UrlDecoder decoder();

    /**
     * Sets the {@link UrlDecoder} used for decoding parameters.
     *
     * @param decoder the decoder to use
     * @return this configurer
     */
    ParamParserConfigurer decoder(UrlDecoder decoder);
}
