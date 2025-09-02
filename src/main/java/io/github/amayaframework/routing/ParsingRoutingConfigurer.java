//package io.github.amayaframework.routing;
//
//import com.github.romanqed.jfunc.Runnable1;
//import com.github.romanqed.jfunc.Runnable2;
//import io.github.amayaframework.context.HttpContext;
//import io.github.amayaframework.filter.FilterSet;
//import io.github.amayaframework.options.OptionSet;
//import io.github.amayaframework.path.parser.PathParser;
//import io.github.amayaframework.router.RouterFactory;
//import io.github.amayaframework.web.WebApplication;
//
//import java.util.Objects;
//import java.util.function.Supplier;
//
/// **
// * Implementation of {@link RoutingConfigurer} that use built-in routing handler.
// * Can be reused after {@link ParsingRoutingConfigurer#createHandler()}
// * or {@link ParsingRoutingConfigurer#run(WebApplication)} invocation.
// */
//public final class ParsingRoutingConfigurer implements RoutingConfigurer {
//    private final RouterFactory factory;
//    private final ParsingPathSet paths;
//    private final Supplier<FilterSet> supplier;
//    private FilterSet filters;
//
//    /**
//     * Constructs a {@link ParsingRoutingConfigurer} instance with given router factory, path parser
//     * and filter set supplier.
//     *
//     * @param factory  the specified router factory, must be non-null
//     * @param parser   the specified parser, must be non-null
//     * @param supplier the specified filter set supplier, must be non-null
//     */
//    public ParsingRoutingConfigurer(RouterFactory factory, PathParser parser, Supplier<FilterSet> supplier) {
//        this.factory = factory;
//        this.paths = new ParsingPathSet(parser);
//        this.supplier = supplier;
//        this.filters = supplier.get();
//    }
//
//    @Override
//    public PathSet getPathSet() {
//        return paths;
//    }
//
//    @Override
//    public FilterSet getFilterSet() {
//        return filters;
//    }
//
//    private RoutingHandler create(OptionSet options) {
//        var router = factory.create(paths.map);
//        if (options == null) {
//            return new RoutingHandler(router, filters);
//        }
//        var handleOptions = options.asKey(RoutingOptions.HANDLE_OPTIONS);
//        var cacheControl = options.get(RoutingOptions.OPTIONS_CACHE_CONTROL);
//        return new RoutingHandler(router, filters, handleOptions, cacheControl);
//    }
//
//    @Override
//    public Runnable2<HttpContext, Runnable1<HttpContext>> createHandler(OptionSet options) {
//        var ret = create(options);
//        filters = supplier.get();
//        paths.clear();
//        return ret;
//    }
//
//    @Override
//    public void run(WebApplication application) {
//        Objects.requireNonNull(application);
//        var group = application.getOptions().getGroup(RoutingOptions.ROUTING_GROUP);
//        var handler = create(group);
//        filters = supplier.get();
//        paths.clear();
//        application.addHandler(handler);
//    }
//}
