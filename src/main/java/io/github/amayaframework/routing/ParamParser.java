package io.github.amayaframework.routing;

import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.path.PathData;

/**
 * Defines a component responsible for extracting and converting route parameters
 * from the HTTP request into strongly typed values.
 * <p>
 * Implementations of this interface are typically used during routing to bind
 * dynamic path segments and query parameters to application-specific types,
 * throwing an {@link IllegalParamException} if a parameter cannot be parsed
 * or validated.
 */
public interface ParamParser {

    /**
     * Parses and validates parameters from the given HTTP request using the
     * metadata contained in the resolved {@link PathData}.
     * <p>
     * Successful parsing populates the request with typed parameter values
     * accessible through its API. If parsing fails, an exception is thrown
     * and the request is considered invalid.
     *
     * @param request the incoming HTTP request
     * @param data    the path metadata describing parameter bindings
     * @throws IllegalParamException if a parameter is missing, malformed, or cannot be converted
     */
    void process(HttpRequest request, PathData data);
}
