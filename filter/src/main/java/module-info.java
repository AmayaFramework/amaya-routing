/**
 * Provides filtering utilities for Amaya Framework.
 * <p>
 * This module defines the {@link io.github.amayaframework.filter.Filter}
 * abstraction for processing raw string values into typed objects,
 * as well as the {@link io.github.amayaframework.filter.FilterSet}
 * API for managing collections of filters. It also includes
 * the {@link io.github.amayaframework.filter.MapFilterSet}
 * implementation.
 * </p>
 *
 * <p>
 * Typical usage is parameter parsing in routing, where path and query
 * parameters are automatically converted into typed values using
 * registered filters.
 * </p>
 */
module amayaframework.filter {
    // Imports
    requires com.github.romanqed.jfunc;
    // Exports
    exports io.github.amayaframework.filter;
}
