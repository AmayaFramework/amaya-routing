package io.github.amayaframework.router;

/**
 *
 */
public final class PathUtil {
    private PathUtil() {
    }

    /**
     * @param path
     * @return
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
