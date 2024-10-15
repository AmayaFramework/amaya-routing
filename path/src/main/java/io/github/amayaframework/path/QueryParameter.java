package io.github.amayaframework.path;

/**
 * A class that represents a query parameter descriptor.
 */
public final class QueryParameter extends Parameter {
    private final Boolean required;

    /**
     * Constructs an {@link QueryParameter} instance with given name, type and requirement flag.
     *
     * @param name     the specified query parameter name, must be non-null
     * @param required the specified requirement flag, may be null
     * @param type     the specified query parameter type, may be null
     */
    public QueryParameter(String name, Boolean required, String type) {
        super(name, type);
        this.required = required;
    }

    /**
     * Checks if this parameter is required.
     *
     * @return boolean value if set, null otherwise
     */
    public Boolean isRequired() {
        return required;
    }
}
