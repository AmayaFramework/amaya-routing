package io.github.amayaframework.router;

import java.util.List;

/**
 *
 */
public final class PathData {
    List<PathParameter> pathParameters;
    List<QueryParameter> queryParameters;

    /**
     * @return
     */
    public List<PathParameter> getPathParameters() {
        return pathParameters;
    }

    /**
     * @param pathParameters
     */
    public void setPathParameters(List<PathParameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * @return
     */
    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    /**
     * @param queryParameters
     */
    public void setQueryParameters(List<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }
}
