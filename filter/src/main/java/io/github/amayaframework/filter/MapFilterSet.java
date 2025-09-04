package io.github.amayaframework.filter;

import com.github.romanqed.jfunc.Exceptions;
import com.github.romanqed.jfunc.Runnable2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link FilterSet} implementation backed by a {@link Map}.
 * Filters are stored as key-value pairs, where the key is a type identifier
 * and the value is the corresponding {@link Filter}.
 *
 * <p>This implementation is mutable but not thread-safe. If concurrent
 * access is required, it should be externally synchronized.</p>
 */
public final class MapFilterSet implements FilterSet {
    private final Map<String, Filter> map;

    /**
     * Creates a new {@link MapFilterSet} backed by the given map.
     *
     * @param map the backing map, must not be {@code null}
     */
    public MapFilterSet(Map<String, Filter> map) {
        this.map = Objects.requireNonNull(map);
    }

    /**
     * Creates a new {@link MapFilterSet} with an empty {@link HashMap}
     * as the underlying storage.
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
    public boolean empty() {
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
        } catch (Throwable e) {
            Exceptions.throwAny(e);
        }
    }
}
