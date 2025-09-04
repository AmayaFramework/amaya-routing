/**
 * Provides URI path model types for Amaya Framework.
 * <p>
 * This module defines descriptors for URI templates, including:
 * </p>
 * <ul>
 *   <li>{@link io.github.amayaframework.path.Path} – normalized path representation</li>
 *   <li>{@link io.github.amayaframework.path.PathParameter} – path parameter descriptor</li>
 *   <li>{@link io.github.amayaframework.path.QueryParameter} – query parameter descriptor</li>
 *   <li>{@link io.github.amayaframework.path.PathData} – container for path and query parameter metadata</li>
 * </ul>
 *
 * <p>
 * These classes are primarily used by the routing system to describe
 * and validate path and query parameters in HTTP requests.
 * </p>
 */
module amayaframework.path {
    // Exports
    exports io.github.amayaframework.path;
}
