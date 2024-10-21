package io.github.amayaframework.filter;

import com.github.romanqed.jfunc.Runnable2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An implementation of the {@link FilterSet} interface that uses a
 * {@link Map} to store filters associated with their types.
 */
public class MapFilterSet implements FilterSet {
    final Map<String, Filter> map;

    /**
     * Constructs a {@link MapFilterSet} instance using the provided supplier
     * to initialize the underlying map.
     *
     * @param supplier a {@link Supplier} that provides a {@link Map}
     *                 instance for storing filters. The supplied map
     *                 cannot be null
     * @throws NullPointerException if the supplier or the map it provides is null
     */
    public MapFilterSet(Supplier<Map<String, Filter>> supplier) {
        this.map = Objects.requireNonNull(supplier.get());
    }

    /**
     * Constructs a {@link MapFilterSet} instance with an empty {@link HashMap}
     * as the underlying storage for filters.
     */
    public MapFilterSet() {
        this.map = new HashMap<>();
    }

    @Override
    public Filter get(String type) {
        return map.get(type);
    }

    @Override
    public boolean contains(String type) {
        return map.containsKey(type);
    }

    @Override
    public void set(String type, Filter filter) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(filter);
        map.put(type, filter);
    }

    @Override
    public void remove(String type) {
        map.remove(type);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<String, Filter> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void forEach(Runnable2<String, Filter> action) {
        try {
            for (var entry : map.entrySet()) {
                action.run(entry.getKey(), entry.getValue());
            }
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
