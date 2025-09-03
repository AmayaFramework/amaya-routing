package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.options.Key;
import io.github.amayaframework.options.OptionSet;
import io.github.amayaframework.web.WebApplication;

import java.util.function.Consumer;

public final class RoutingApplicationConfigurer implements Runnable1<WebApplication> {
    private final RoutingBuilder builder;
    private final boolean configure;

    public RoutingApplicationConfigurer(RoutingBuilder builder, boolean configure) {
        this.builder = builder;
        this.configure = configure;
    }

    public RoutingConfigurer getConfigurer() {
        return builder;
    }

    private static <T> void configure(OptionSet set, Key<T> key, Consumer<T> cons) {
        var value = set.get(key);
        if (value != null) {
            cons.accept(value);
        }
    }

    public void configure(OptionSet options) {
        var paramParser = options.get(RoutingOptions.PARAM_PARSER);
        if (paramParser != null) {
            builder.paramParser(paramParser);
        } else {
            var cfg = builder.paramParserConfigurer();
            configure(options, RoutingOptions.FILTER_SET, cfg::filterSet);
            configure(options, RoutingOptions.URL_DECODER, cfg::decoder);
            configure(options, RoutingOptions.DECODE_QUERY, cfg::decodeQuery);
            configure(options, RoutingOptions.DECODE_PATH, cfg::decodePath);
        }
        configure(options, RoutingOptions.HANDLE_OPTIONS, builder::handleOptionsRequest);
        configure(options, RoutingOptions.CACHE_CONTROL, builder::cacheControl);
    }

    @Override
    public void run(WebApplication app) {
        if (configure) {
            var options = app.options().getGroup(RoutingOptions.ROUTING_GROUP);
            if (options != null) {
                configure(options);
            }
        }
        app.configurer().add(builder.build());
    }
}
