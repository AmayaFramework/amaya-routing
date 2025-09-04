/**
 * Provides routing abstractions for Amaya Framework.
 * <p>
 * This module defines the {@link io.github.amayaframework.router.Router}
 * API for mapping normalized URI paths to {@link io.github.amayaframework.router.PathContext}
 * values, as well as supporting utilities and factories.
 * </p>
 *
 * <h2>Key components</h2>
 * <ul>
 *   <li>{@link io.github.amayaframework.router.Router} – interface for path resolution</li>
 *   <li>{@link io.github.amayaframework.router.RouterFactory} – factory for creating routers</li>
 *   <li>{@link io.github.amayaframework.router.PathContext} – container for resolved path metadata and context value</li>
 *   <li>{@link io.github.amayaframework.router.PathUtil} – path normalization utilities</li>
 *   <li>{@link io.github.amayaframework.router.StaticRouterFactory} – simple implementation for static-only routes</li>
 * </ul>
 *
 * <p>
 * More advanced router implementations (FSM, tree-based) are available
 * in dedicated submodules of the framework.
 * </p>
 */
module amayaframework.router {
    // Imports
    requires amayaframework.path;
    requires amayaframework.tokenize;
    // Exports
    exports io.github.amayaframework.router;
}
