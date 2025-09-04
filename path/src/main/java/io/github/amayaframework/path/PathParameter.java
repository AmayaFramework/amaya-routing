package io.github.amayaframework.path;

/**
 * Represents a path parameter descriptor used in URI templates.
 * <p>
 * Example: in {@code /users/{id}}, the {@code id} is a path parameter.
 */
public final class PathParameter extends Parameter {
    private final int index;

    /**
     * Constructs a {@link PathParameter} instance.
     *
     * @param name  the parameter name, must be non-null
     * @param index the position of the parameter in the path, must be &gt;= 0
     * @param type  the type identifier for the parameter, may be null
     */
    public PathParameter(String name, int index, String type) {
        super(name, type);
        this.index = index;
    }

    /**
     * Gets the position of the parameter in the path.
     *
     * @return the parameter index, starting at 0
     */
    public int getIndex() {
        return index;
    }
}
