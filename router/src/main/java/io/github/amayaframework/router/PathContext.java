package io.github.amayaframework.router;

import io.github.amayaframework.path.PathData;

/**
 * Holds the routing context for a resolved path, including
 * parsed {@link PathData} and an associated value.
 *
 * @param <T> the type of the value stored in the context
 */
public final class PathContext<T> {
    final PathData data;
    final T value;

    /**
     * Creates a new {@link PathContext}.
     *
     * @param data  optional {@link PathData}, may be {@code null}
     * @param value context value, may be {@code null}
     */
    public PathContext(PathData data, T value) {
        this.data = data;
        this.value = value;
    }

    /**
     * Returns the parsed {@link PathData}, or {@code null} if none.
     *
     * @return the {@link PathData} instance
     */
    public PathData getData() {
        return data;
    }

    /**
     * Returns the associated context value.
     *
     * @return the path context value
     */
    public T getValue() {
        return value;
    }
}
