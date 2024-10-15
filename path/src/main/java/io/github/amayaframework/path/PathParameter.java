package io.github.amayaframework.path;

/**
 * A class that represents a path parameter descriptor.
 */
public final class PathParameter extends Parameter {
    int index;

    /**
     * Constructs an {@link PathParameter} instance with given name, type and parameter index.
     *
     * @param name  the specified path parameter name, must be non-null
     * @param index the specified parameter index, must be &gt;= 0
     * @param type  the specified path parameter type, may be null
     */
    public PathParameter(String name, int index, String type) {
        super(name, type);
        this.index = index;
    }

    /**
     * Gets path parameter position in path.
     *
     * @return the parameter position
     */
    public int getIndex() {
        return index;
    }
}
