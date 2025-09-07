/**
 * Provides the core routing infrastructure for Amaya Framework.
 * <p>
 * This module integrates path parsing, HTTP method dispatching,
 * and parameter parsing into a configurable routing pipeline.
 * <p>
 * Key features include:
 * <ul>
 *   <li>{@link io.github.amayaframework.routing.MethodMap} – mapping of HTTP methods to handlers</li>
 *   <li>{@link io.github.amayaframework.routing.RouteBuilder} / {@link io.github.amayaframework.routing.RouterBuilder} – fluent route and router builders</li>
 *   <li>{@link io.github.amayaframework.routing.RoutingBuilder} – assembly of complete routing pipelines</li>
 *   <li>{@link io.github.amayaframework.routing.ParamParser} – pluggable parameter parsing with filter support</li>
 *   <li>{@link io.github.amayaframework.routing.RoutingApplicationConfigurer} – integration with {@code amayaframework.web}</li>
 * </ul>
 *
 * <h2>Dependencies</h2>
 * <ul>
 *   <li>Path parsing – {@code amayaframework.path}, {@code amayaframework.path.parser}</li>
 *   <li>Filtering – {@code amayaframework.filter}</li>
 *   <li>Routing core – {@code amayaframework.router}</li>
 *   <li>Options &amp; web integration – {@code amayaframework.options}, {@code amayaframework.web}</li>
 * </ul>
 */
module amayaframework.routing {
    // Imports
    // Basic dependencies
    requires com.github.romanqed.jtype;
    requires com.github.romanqed.juni;
    requires amayaframework.tokenize;
    // Filters
    requires amayaframework.filter;
    // Path
    requires amayaframework.path;
    requires amayaframework.path.parser;
    // Router
    requires amayaframework.router;
    // Amaya modules
    requires amayaframework.options;
    requires amayaframework.web;
    // Exports
    exports io.github.amayaframework.routing;
}
