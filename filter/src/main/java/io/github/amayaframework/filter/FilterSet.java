package io.github.amayaframework.filter;

import com.github.romanqed.jfunc.Runnable2;

import java.util.Map;

/**
 * An interface describing a collection of {@link Filter} instances
 * associated with string-based types. A {@link FilterSet} is used
 * to register, retrieve, and manage filters that convert raw values
 * into typed objects during parameter parsing.
 */
public interface FilterSet {

    /**
     * Gets the {@link Filter} associated with the given type.
     *
     * @param type the specified type to look up
     * @return the associated {@link Filter}, or {@code null} if not found
     */
    Filter get(String type);

    /**
     * Checks whether a {@link Filter} exists for the specified type.
     *
     * @param type the type to check
     * @return {@code true} if a filter exists, {@code false} otherwise
     */
    boolean contains(String type);

    /**
     * Sets (or replaces) a {@link Filter} for the specified type.
     *
     * @param type   the type identifier to associate with the filter
     * @param filter the {@link Filter} instance to register
     */
    void set(String type, Filter filter);

    /**
     * Removes the {@link Filter} associated with the specified type.
     *
     * @param type the type whose filter should be removed
     */
    void remove(String type);

    /**
     * Checks whether this filter set is empty.
     *
     * @return {@code true} if no filters are registered, {@code false} otherwise
     */
    boolean empty();

    /**
     * Returns the number of filters in this set.
     *
     * @return the filter count
     */
    int size();

    /**
     * Removes all filters from this set.
     */
    void clear();

    /**
     * Returns an unmodifiable {@link Map} view of all filters in this set.
     *
     * @return a map of type identifiers to {@link Filter} instances
     */
    Map<String, Filter> asMap();

    /**
     * Applies the given action to each entry in this set.
     *
     * @param action a {@link Runnable2} consuming the type and its filter
     */
    void forEach(Runnable2<String, Filter> action);
}
