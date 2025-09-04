package io.github.amayaframework.router.tree;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.router.PathContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal node of the path segment tree.
 *
 * <p>Each node can contain:</p>
 * <ul>
 *   <li>a map of literal child segments ({@link #nodes})</li>
 *   <li>a single "generic" child node ({@link #generic}) for dynamic segments
 *   (represented in {@link Path} by {@code null})</li>
 *   <li>an optional {@link PathContext} bound to the full path
 *   that ends at this node</li>
 * </ul>
 *
 * <p>Used by {@link TreeRouter} to resolve dynamic routes.</p>
 */
@SuppressWarnings("rawtypes")
final class PathNode {
    private Map<String, PathNode> nodes;
    private PathNode generic;
    private PathContext context;

    /**
     * Attaches or returns an existing generic (dynamic) child node.
     *
     * @return the attached or existing generic child node
     */
    PathNode attach() {
        if (generic == null) {
            generic = new PathNode();
        }
        return generic;
    }

    /**
     * Attaches or returns a child node for a literal segment.
     *
     * @param segment the path segment
     * @return the attached or existing child node
     */
    PathNode attach(String segment) {
        if (nodes == null) {
            nodes = new HashMap<>();
        }
        return nodes.computeIfAbsent(segment, k -> new PathNode());
    }

    /**
     * Attaches a full {@link Path} and associates it with the given context.
     *
     * <p>Dynamic segments (parameters) are represented by {@code null} in the segment list,
     * and will be attached as {@link #generic} nodes.</p>
     *
     * @param path    the path to attach
     * @param context the path context to associate
     */
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

    /**
     * Looks up a child node by literal or generic segment.
     *
     * @param segment the segment to look up
     * @return the matching child node, or {@code null} if none
     */
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

    /**
     * Resolves a path by traversing the tree according to the provided segments.
     *
     * @param segments the sequence of path segments
     * @return the resolved {@link PathContext}, or {@code null} if no match
     */
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
