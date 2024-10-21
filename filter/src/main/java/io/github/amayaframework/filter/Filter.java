package io.github.amayaframework.filter;

/**
 * An interface describing an abstract filter that converts a string into an object representation.
 */
public interface Filter {

    /**
     * Processes raw string and converts it into an object representation.
     *
     * @param raw the
     * @return the {@link Object} representation
     */
    Object process(String raw);
}
