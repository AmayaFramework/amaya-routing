package io.github.amayaframework.router.tree;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.router.PathContext;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
final class PathNode {
    private Map<String, PathNode> nodes;
    private PathNode generic;
    private PathContext context;

    PathNode attach() {
        if (generic != null) {
            return generic;
        }
        var ret = new PathNode();
        generic = ret;
        return ret;
    }

    PathNode attach(String segment) {
        if (nodes == null) {
            nodes = new HashMap<>();
        }
        return nodes.computeIfAbsent(segment, k -> new PathNode());
    }

    void attach(Path path, PathContext context) {
        var segments = path.getSegments();
        if (segments.isEmpty()) {
            this.context = context;
        }
        var current = this;
        for (var segment : segments) {
            if (segment == null) {
                current = current.attach();
            } else {
                current = current.attach(segment);
            }
        }
        current.context = context;
    }

    PathNode lookup(String segment) {
        if (nodes == null) {
            return generic;
        }
        var found = nodes.get(segment);
        if (found == null) {
            return generic;
        }
        return found;
    }

    PathContext lookup(Iterable<String> segments) {
        var current = this;
        for (var segment : segments) {
            current = current.lookup(segment);
            if (current == null) {
                return null;
            }
        }
        return current.context;
    }
}
