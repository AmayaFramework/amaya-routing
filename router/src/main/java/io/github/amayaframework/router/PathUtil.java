package io.github.amayaframework.router;

/**
 * The utility class, containing path-related methods.
 */
public final class PathUtil {
    private PathUtil() {
    }

    /**
     * Normalizes given path by following rules:
     * <br>
     * 1. Path must start with a slash
     * 2. Path must end with no slash
     * 3. If path is empty, path must be '/'
     *
     * @param path the specified path to be normalized
     * @return the normalized path
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
        return "/" + path;
    }
}
