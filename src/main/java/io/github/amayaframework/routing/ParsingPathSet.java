package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.parser.PathParser;
import io.github.amayaframework.path.Path;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class ParsingPathSet implements PathSet {
    final Map<Path, Map<HttpMethod, Runnable1<HttpContext>>> map;
    final Map<String, Path> cache;
    private final PathParser parser;

    ParsingPathSet(PathParser parser) {
        this.parser = parser;
        this.map = new HashMap<>();
        this.cache = new HashMap<>();
    }

    private static String getPathPart(String path) {
        var index = path.indexOf('?');
        if (index < 0) {
            return path;
        }
        return path.substring(0, index);
    }

    private static Runnable1<HttpContext> getHandler(Map<HttpMethod, Runnable1<HttpContext>> map, HttpMethod method) {
        if (map == null) {
            return null;
        }
        return map.get(method);
    }

    private static void removeHandler(Map<HttpMethod, Runnable1<HttpContext>> map, HttpMethod method) {
        if (map == null) {
            return;
        }
        map.remove(method);
    }

    @Override
    public Runnable1<HttpContext> get(HttpMethod method, String path) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(path);
        var parsed = cache.get(getPathPart(path));
        if (parsed != null) {
            return getHandler(map.get(parsed), method);
        }
        parsed = parser.parse(path);
        return getHandler(map.get(parsed), method);
    }

    @Override
    public void set(HttpMethod method, String path, Runnable1<HttpContext> handler) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(path);
        Objects.requireNonNull(handler);
        var parsed = parser.parse(path);
        cache.put(getPathPart(path), parsed);
        var handlers = map.computeIfAbsent(parsed, k -> new HashMap<>());
        handlers.put(method, handler);
    }

    @Override
    public void remove(HttpMethod method, String path) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(path);
        var parsed = cache.remove(getPathPart(path));
        if (parsed != null) {
            removeHandler(map.get(parsed), method);
            return;
        }
        parsed = parser.parse(path);
        removeHandler(map.get(parsed), method);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
        cache.clear();
    }
}
