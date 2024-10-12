package io.github.amayaframework.router;

import java.util.List;

/**
 *
 */
public final class PathData {
    final List<PathParameter> pathParameters;
    final List<QueryParameter> queryParameters;

    /**
     * @param pathParameters
     * @param queryParameters
     */
    public PathData(List<PathParameter> pathParameters, List<QueryParameter> queryParameters) {
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
    }

    /**
     * @return
     */
    public List<PathParameter> getPathParameters() {
        return pathParameters;
    }

    /**
     * @return
     */
    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }
}
