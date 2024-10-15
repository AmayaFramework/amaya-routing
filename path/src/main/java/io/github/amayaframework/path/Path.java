package io.github.amayaframework.path;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A class that represents a universal uri path descriptor.
 */
public final class Path {
    private final String path;
    private final List<String> segments;
    private final boolean dynamic;
    private PathData data;

    /**
     * Constructs a {@link Path} instance with given normalized string representation, path segments and dynamic flag.
     *
     * @param path     the specified normalized path representation, must be non-null
     * @param segments the specified path segments, must be non-null
     * @param dynamic  the specified dynamic flag
     */
    public Path(String path, List<String> segments, boolean dynamic) {
        this.path = Objects.requireNonNull(path);
        this.segments = Collections.unmodifiableList(segments);
        this.dynamic = dynamic;
    }

    /**
     * Gets normalized path representation.
     *
     * @return the normalized path representation
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets path segments.
     *
     * @return the {@link List} containing path segments
     */
    public List<String> getSegments() {
        return segments;
    }

    /**
     * Checks if this path is dynamic.
     *
     * @return true if this path is dynamic, false otherwise
     */
    public boolean isDynamic() {
        return dynamic;
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
     * Sets path data.
     *
     * @param data the {@link PathData} instance
     */
    public void setData(PathData data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var that = (Path) object;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return path;
    }
}
