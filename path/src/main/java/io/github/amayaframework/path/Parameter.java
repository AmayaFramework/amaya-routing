package io.github.amayaframework.path;

/**
 * A class that represents a universal parameter descriptor.
 */
public class Parameter {
    protected final String name;
    protected final String type;

    /**
     * Constructs a {@link Parameter} instance with given name and type.
     *
     * @param name the specified parameter name, must be non-null
     * @param type the specified parameter type, may be null
     */
    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets parameter name.
     *
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets parameter type.
     *
     * @return the parameter type
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
