package io.github.amayaframework.router;

import java.util.List;

/**
 *
 */
public final class Path {
    final String path;
    final List<String> segments;
    final boolean dynamic;
    PathData data;

    /**
     * @param path
     * @param segments
     * @param dynamic
     */
    public Path(String path, List<String> segments, boolean dynamic) {
        this.path = path;
        this.segments = segments;
        this.dynamic = dynamic;
    }

    /**
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * @return
     */
    public List<String> getSegments() {
        return segments;
    }

    /**
     * @return
     */
    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * @return
     */
    public PathData getData() {
        return data;
    }

    /**
     * @param data
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
