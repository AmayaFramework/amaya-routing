package io.github.amayaframework.filter;

/**
 * Represents a transformation filter that processes a raw {@link String}
 * value into another representation. Filters are typically used for
 * parsing and converting path or query parameters into typed values.
 *
 * <p>Filters should be stateless and thread-safe, as they can be reused
 * across multiple requests concurrently.</p>
 */
@FunctionalInterface
public interface Filter {

    /**
     * Processes the given raw string value and returns the transformed result.
     *
     * @param raw the raw string input to be processed, never {@code null}
     * @return the transformed object, possibly of a specific type depending on the filter
     * @throws RuntimeException if the input cannot be processed or converted
     */
    Object process(String raw);
}
