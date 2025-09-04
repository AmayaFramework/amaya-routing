package io.github.amayaframework.router;

/**
 * Utility methods for working with path strings.
 */
public final class PathUtil {
    private PathUtil() {
    }

    /**
     * Normalizes the given path string according to the following rules:
     * <ul>
     *   <li>Path must start with a slash</li>
     *   <li>Path must not end with a trailing slash</li>
     *   <li>If path is empty, it becomes {@code "/"}</li>
     * </ul>
     *
     * @param path raw path string
     * @return normalized path string
     */
    public static String normalize(String path) {
        if (path.isEmpty() || path.equals("/")) {
            return "/";
        }
        var last = path.length() - 1;
        if (path.charAt(last) == '/') {
            path = path.substring(0, last);
        }
        if (path.charAt(0) == '/') {
            return path;
        }
        return '/' + path;
    }
}
