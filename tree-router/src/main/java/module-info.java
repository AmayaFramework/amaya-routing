/**
 * Provides a tree-based router implementation for AmayaFramework.
 *
 * <p>This module contains classes for building and using routers
 * based on a path segment tree. The tree router supports both:</p>
 * <ul>
 *   <li>Static routes — resolved instantly through a map lookup</li>
 *   <li>Dynamic routes — resolved by traversing the segment tree</li>
 * </ul>
 *
 * <p>Main entry point: {@link io.github.amayaframework.router.tree.TreeRouterFactory}.</p>
 */
module amayaframework.router.tree {
    // Imports
    requires amayaframework.router;
    requires amayaframework.path;
    requires amayaframework.tokenize;
    // Exports
    exports io.github.amayaframework.router.tree;
}
