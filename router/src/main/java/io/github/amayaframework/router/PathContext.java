package io.github.amayaframework.router;

import io.github.amayaframework.path.PathData;

/**
 * A class representing the context of the path.
 *
 * @param <T> the path context value type
 */
public final class PathContext<T> {
    final PathData data;
    final T value;

    /**
     * Constructs a {@link PathContext} instance with given path data and context value.
     *
     * @param data  the specified {@link PathData} instance, may be null
     * @param value the specified context value, may be null
     */
    public PathContext(PathData data, T value) {
        this.data = data;
        this.value = value;
    }

    /**
     * Gets path data.
     *
     * @return the {@link PathData} instance
     */
    public PathData getData() {
        return data;
    }

    /**
     * Gets path context value.
     *
     * @return the path context value
     */
    public T getValue() {
        return value;
    }
}
