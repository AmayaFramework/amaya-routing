/**
 * Provides URI template parsers for Amaya Framework.
 * <p>
 * This module defines abstractions and implementations for parsing path templates,
 * path parameters, and query parameters. The main entry point is
 * {@link io.github.amayaframework.path.parser.PathParser}, with helpers
 * in {@link io.github.amayaframework.path.parser.PathParsers}.
 * </p>
 *
 * <h2>Key components</h2>
 * <ul>
 *   <li>{@link io.github.amayaframework.path.parser.PathParser} – interface for parsing full path templates</li>
 *   <li>{@link io.github.amayaframework.path.parser.PathParameterParser} – interface for parsing path parameters</li>
 *   <li>{@link io.github.amayaframework.path.parser.QueryParameterParser} – interface for parsing query parameters</li>
 *   <li>{@link io.github.amayaframework.path.parser.BracketPathParser} – parser supporting bracketed parameters</li>
 *   <li>{@link io.github.amayaframework.path.parser.TypedPathParameterParser} – parser for typed path parameters</li>
 *   <li>{@link io.github.amayaframework.path.parser.TypedQueryParameterParser} – parser for typed query parameters</li>
 *   <li>{@link io.github.amayaframework.path.parser.PathParsers} – utility factory for common parser configurations</li>
 * </ul>
 */
module amayaframework.path.parser {
    // Imports
    requires amayaframework.path;
    requires amayaframework.tokenize;
    // Exports
    exports io.github.amayaframework.path.parser;
}
