package io.github.amayaframework.path;

import java.util.List;

/**
 * Holds metadata for a {@link Path}, including its
 * {@link PathParameter} and {@link QueryParameter} descriptors.
 */
public final class PathData {
    private List<PathParameter> pathParams;
    private List<QueryParameter> queryParams;

    /**
     * Gets path parameter descriptors.
     *
     * @return the {@link List} of {@link PathParameter} descriptors, or {@code null} if none
     */
    public List<PathParameter> getPathParams() {
        return pathParams;
    }

    /**
     * Sets path parameter descriptors.
     *
     * @param pathParameters the {@link List} of {@link PathParameter} descriptors
     */
    public void setPathParams(List<PathParameter> pathParameters) {
        this.pathParams = pathParameters;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @return the {@link List} of {@link QueryParameter} descriptors, or {@code null} if none
     */
    public List<QueryParameter> getQueryParams() {
        return queryParams;
    }

    /**
     * Sets query parameter descriptors.
     *
     * @param queryParams the {@link List} of {@link QueryParameter} descriptors
     */
    public void setQueryParams(List<QueryParameter> queryParams) {
        this.queryParams = queryParams;
    }
}
