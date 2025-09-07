package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.options.Key;

/**
 * Defines the set of option keys used to configure routing.
 * <p>
 * These options may be provided via the application's {@link io.github.amayaframework.options.OptionSet}
 * under the {@link #ROUTING_GROUP} group.
 */
public final class RoutingOptions {
    private RoutingOptions() {
    }

    /**
     * The name of the routing option group.
     */
    public static final String ROUTING_GROUP = "routing";

    /**
     * The key for providing a custom {@link ParamParser}.
     * <br>
     * Required type: {@link ParamParser}.
     */
    public static final Key<ParamParser> PARAM_PARSER = Key.of("param_parser", ParamParser.class);

    /**
     * The key for enabling query string decoding.
     * <br>
     * Required type: {@link Boolean}.
     */
    public static final Key<Boolean> DECODE_QUERY = Key.of("decode_query", Boolean.class);

    /**
     * The key for enabling path segment decoding.
     * <br>
     * Required type: {@link Boolean}.
     */
    public static final Key<Boolean> DECODE_PATH = Key.of("decode_path", Boolean.class);

    /**
     * The key for supplying a custom {@link UrlDecoder}.
     * <br>
     * Required type: {@link UrlDecoder}.
     */
    public static final Key<UrlDecoder> URL_DECODER = Key.of("url_decoder", UrlDecoder.class);

    /**
     * The key for supplying a custom {@link FilterSet}.
     * <br>
     * Required type: {@link FilterSet}.
     */
    public static final Key<FilterSet> FILTER_SET = Key.of("filter_set", FilterSet.class);

    /**
     * The key for the flag that determines whether the routing handler
     * should automatically handle {@code OPTIONS} requests.
     * <br>
     * Required type: {@link Boolean}.
     */
    public static final Key<Boolean> HANDLE_OPTIONS = Key.of("handle_options", Boolean.class);

    /**
     * The key for specifying a value for the {@code Cache-Control} response header.
     * <br>
     * Required type: {@link String}.
     */
    public static final Key<String> CACHE_CONTROL = Key.of("cache_control", String.class);
}
