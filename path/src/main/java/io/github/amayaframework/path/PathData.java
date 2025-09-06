package io.github.amayaframework.path;

/**
 * Holds metadata for a {@link Path}, including its
 * {@link PathParameter} and {@link QueryParameter} descriptors.
 */
public final class PathData {
    private PathParameter[] pathParams;
    private QueryParameter[] queryParams;

    /**
     * Gets path parameter descriptors.
     *
     * @return the array of {@link PathParameter} descriptors, or {@code null} if none
     */
    public PathParameter[] getPathParams() {
        return pathParams;
    }

    /**
     * Sets path parameter descriptors.
     *
     * @param pathParams the array of {@link PathParameter} descriptors
     */
    public void setPathParams(PathParameter[] pathParams) {
        this.pathParams = pathParams;
    }

    /**
     * Gets query parameter descriptors.
     *
     * @return the array of {@link QueryParameter} descriptors, or {@code null} if none
     */
    public QueryParameter[] getQueryParams() {
        return queryParams;
    }

    /**
     * Sets query parameter descriptors.
     *
     * @param queryParams the array of {@link QueryParameter} descriptors
     */
    public void setQueryParams(QueryParameter[] queryParams) {
        this.queryParams = queryParams;
    }
}
