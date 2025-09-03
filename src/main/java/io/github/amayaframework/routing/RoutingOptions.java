package io.github.amayaframework.routing;

import io.github.amayaframework.filter.FilterSet;
import io.github.amayaframework.options.Key;

public final class RoutingOptions {
    private RoutingOptions() {
    }

    public static final String ROUTING_GROUP = "routing";

    public static final Key<ParamParser> PARAM_PARSER = Key.of("param_parser", ParamParser.class);

    public static final Key<Boolean> DECODE_QUERY = Key.of("decode_query", Boolean.class);

    public static final Key<Boolean> DECODE_PATH = Key.of("decode_path", Boolean.class);

    public static final Key<UrlDecoder> URL_DECODER = Key.of("url_decoder", UrlDecoder.class);

    public static final Key<FilterSet> FILTER_SET = Key.of("filter_set", FilterSet.class);

    /**
     * The key for the flag determines whether the routing handler will handle OPTIONS request by default.
     * <br>
     * Required type: {@link Boolean}.
     */
    public static final Key<Boolean> HANDLE_OPTIONS = Key.of("handle_options", Boolean.class);

    /**
     * The key for the cache control header.
     * <br>
     * Required type: {@link String}.
     */
    public static final Key<String> CACHE_CONTROL = Key.of("cache_control", String.class);
}
