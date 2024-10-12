package io.github.amayaframework.router;

/**
 * @param <T>
 */
public final class PathContext<T> {
    final PathData data;
    final T value;

    /**
     * @param data
     * @param value
     */
    public PathContext(PathData data, T value) {
        this.data = data;
        this.value = value;
    }

    /**
     * @return
     */
    public PathData getData() {
        return data;
    }

    /**
     * @return
     */
    public T getValue() {
        return value;
    }
}
