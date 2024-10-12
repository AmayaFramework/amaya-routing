package io.github.amayaframework.router;

/**
 *
 */
public final class QueryParameter extends Parameter {
    Boolean required;

    /**
     * @param name
     * @param required
     * @param type
     */
    public QueryParameter(String name, Boolean required, String type) {
        super(name, type);
        this.required = required;
    }

    /**
     * @return
     */
    public Boolean isRequired() {
        return required;
    }
}
