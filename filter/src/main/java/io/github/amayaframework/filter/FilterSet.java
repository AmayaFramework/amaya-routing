package io.github.amayaframework.filter;

import com.github.romanqed.jfunc.Runnable2;

import java.util.Map;

/**
 * An interface describing an abstract set of filters associated with types.
 */
public interface FilterSet {

    /**
     * Gets filter associated with given type.
     *
     * @param type the specified type to filter lookup
     * @return the {@link Filter} instance or null, if not found
     */
    Filter get(String type);

    /**
     * Checks if a filter exists for the specified type.
     *
     * @param type the specified type to check
     * @return true if a filter exists for the specified type, false otherwise
     */
    boolean contains(String type);

    /**
     * Sets a filter for the specified type. If a filter already exists for
     * that type, it will be replaced.
     *
     * @param type   the specified type to associate with the filter
     * @param filter the {@link Filter} instance to be associated with the specified type
     */
    void set(String type, Filter filter);

    /**
     * Removes the filter associated with the specified type.
     *
     * @param type the specified type whose filter should be removed
     */
    void remove(String type);

    /**
     * Checks if there are any filters in this set.
     *
     * @return true if there are no filters in this set, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the number of filters in this set.
     *
     * @return the number of filters in this set
     */
    int size();

    /**
     * Clears all filters from this set, removing any existing associations
     */
    void clear();

    /**
     * Returns a map representation of the filters in this set, where
     * each entry consists of a type and its associated filter.
     *
     * @return a map containing all types and their corresponding {@link Filter} instances
     */
    Map<String, Filter> asMap();

    /**
     * Performs the given action for each entry in this filter set.
     *
     * @param action a {@link Runnable2} that takes a type and its associated filter,
     *               to be executed for each entry in the filter set
     */
    void forEach(Runnable2<String, Filter> action);
}
