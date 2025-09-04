package io.github.amayaframework.path;

/**
 * Represents a query parameter descriptor used in URI templates.
 * <p>
 * Example: in {@code ?page=2}, the {@code page} is a query parameter.
 */
public final class QueryParameter extends Parameter {
    private final Boolean required;

    /**
     * Constructs a {@link QueryParameter} instance.
     *
     * @param name     the parameter name, must be non-null
     * @param required whether this parameter is required, may be null
     * @param type     the type identifier for the parameter, may be null
     */
    public QueryParameter(String name, Boolean required, String type) {
        super(name, type);
        this.required = required;
    }

    /**
     * Returns the required flag for this parameter.
     *
     * @return {@code Boolean.TRUE} if required, {@code Boolean.FALSE} if optional,
     * or {@code null} if unspecified
     */
    public Boolean isRequired() {
        return required;
    }
}
