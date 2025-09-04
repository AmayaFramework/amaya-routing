package io.github.amayaframework.path;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a normalized URI path descriptor.
 * <p>
 * A {@link Path} stores the normalized path string, its pre-tokenized
 * segments, and a flag indicating whether it contains dynamic parameters.
 * Optional {@link PathData} may be attached for parameter metadata.
 */
public final class Path {
    private final String path;
    private final List<String> segments;
    private final boolean dynamic;
    private PathData data;

    /**
     * Constructs a {@link Path} instance.
     *
     * @param path     the normalized path string, must be non-null
     * @param segments the list of path segments, must be non-null
     * @param dynamic  whether this path contains dynamic parameters
     */
    public Path(String path, List<String> segments, boolean dynamic) {
        this.path = Objects.requireNonNull(path);
        this.segments = Collections.unmodifiableList(segments);
        this.dynamic = dynamic;
    }

    /**
     * Gets the normalized path string.
     *
     * @return the normalized path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the list of path segments.
     *
     * @return an unmodifiable {@link List} of path segments
     */
    public List<String> getSegments() {
        return segments;
    }

    /**
     * Checks whether this path contains dynamic parameters.
     *
     * @return {@code true} if the path contains parameters, {@code false} otherwise
     */
    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Gets the associated {@link PathData}, if any.
     *
     * @return the {@link PathData} instance, or {@code null} if none
     */
    public PathData getData() {
        return data;
    }

    /**
     * Associates {@link PathData} with this path.
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
