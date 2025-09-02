package io.github.amayaframework.path;

import java.util.List;

/**
 * A class representing a set of path data: path and query parameters descriptors.
 */
public final class PathData {
    private List<PathParameter> pathParams;
    private List<QueryParameter> queryParams;

    /**
     * Gets path parameter descriptors.
     *
     * @return the {@link List} containing path parameter descriptors
     */
    public List<PathParameter> getPathParams() {
        return pathParams;
    }

    /**
     * Sets path parameter descriptors.
     *
     * @param pathParameters the {@link List} containing path parameter descriptors
     */
    public void setPathParams(List<PathParameter> pathParameters) {
        this.pathParams = pathParameters;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @return the {@link List} containing query parameter descriptors
     */
    public List<QueryParameter> getQueryParams() {
        return queryParams;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @param queryParams the {@link List} containing query parameter descriptors
     */
    public void setQueryParams(List<QueryParameter> queryParams) {
        this.queryParams = queryParams;
    }
}
