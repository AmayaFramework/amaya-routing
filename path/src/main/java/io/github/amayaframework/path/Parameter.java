package io.github.amayaframework.path;

/**
 * Represents a universal parameter descriptor used in URI templates.
 * <p>
 * Serves as a base class for {@link PathParameter} and {@link QueryParameter}.
 * Each parameter has a name and an optional type identifier
 * that can be resolved using a {@code io.github.amayaframework.filter.Filter}.
 */
public class Parameter {
    protected final String name;
    protected final String type;

    /**
     * Constructs a {@link Parameter} instance with the given name and type.
     *
     * @param name the parameter name, must be non-null
     * @param type the parameter type identifier, may be null
     */
    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the parameter name.
     *
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parameter type identifier.
     *
     * @return the parameter type identifier, or {@code null} if none
     */
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Parameter)) return false;
        var parameter = (Parameter) object;
        return name.equals(parameter.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + ":" + type;
    }
}
