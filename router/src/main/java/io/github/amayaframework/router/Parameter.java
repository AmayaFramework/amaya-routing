package io.github.amayaframework.router;

/**
 *
 */
public class Parameter {
    final String name;
    final String type;

    /**
     * @param name
     * @param type
     */
    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
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
