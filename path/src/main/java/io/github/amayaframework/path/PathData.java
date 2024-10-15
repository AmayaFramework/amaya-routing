package io.github.amayaframework.path;

import java.util.List;

/**
 * A class representing a set of path data: path and query parameters descriptors.
 */
public final class PathData {
    private List<PathParameter> pathParameters;
    private List<QueryParameter> queryParameters;

    /**
     * Gets path parameter descriptors.
     *
     * @return the {@link List} containing path parameter descriptors
     */
    public List<PathParameter> getPathParameters() {
        return pathParameters;
    }

    /**
     * Sets path parameter descriptors.
     *
     * @param pathParameters the {@link List} containing path parameter descriptors
     */
    public void setPathParameters(List<PathParameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @return the {@link List} containing query parameter descriptors
     */
    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @param queryParameters the {@link List} containing query parameter descriptors
     */
    public void setQueryParameters(List<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }
}
