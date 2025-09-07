package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.options.Key;
import io.github.amayaframework.options.OptionSet;
import io.github.amayaframework.web.WebApplication;

import java.util.function.Consumer;

/**
 * A configurator that integrates {@link RoutingBuilder} into a {@link WebApplication}.
 * <p>
 * Optionally applies {@link RoutingOptions} from the application's {@link OptionSet},
 * then registers the constructed routing task in the application's configurer.
 */
public final class RoutingApplicationConfigurer implements Runnable1<WebApplication> {
    private final RoutingBuilder builder;
    private final boolean configure;

    /**
     * Creates a new application configurator.
     *
     * @param builder   the underlying {@link RoutingBuilder} used to construct routing tasks
     * @param configure whether to apply {@link RoutingOptions} from the application
     */
    public RoutingApplicationConfigurer(RoutingBuilder builder, boolean configure) {
        this.builder = builder;
        this.configure = configure;
    }

    /**
     * Returns the underlying {@link RoutingConfigurer} for direct configuration.
     *
     * @return the routing configurer
     */
    public RoutingConfigurer getConfigurer() {
        return builder;
    }

    private static <T> void configure(OptionSet set, Key<T> key, Consumer<T> cons) {
        var value = set.get(key);
        if (value != null) {
            cons.accept(value);
        }
    }

    /**
     * Applies routing-related options from the provided {@link OptionSet}.
     * <p>
     * Supports custom {@link ParamParser}, or fallback to filter set, URL decoder,
     * and decoding flags. Also configures global options such as handling
     * {@code OPTIONS} requests and {@code Cache-Control} headers.
     *
     * @param options the option set containing routing configuration
     */
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

    /**
     * Applies routing configuration (if enabled) and registers
     * the built routing task in the {@link WebApplication}.
     *
     * @param app the target web application
     */
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
