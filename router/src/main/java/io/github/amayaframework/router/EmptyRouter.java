package io.github.amayaframework.router;

import java.util.function.Supplier;

/**
 * A {@link Router} implementation that does not resolve any paths.
 * <p>
 * This router always returns {@code null} for any input. It can be used
 * as a placeholder or default router when no routing is required.
 *
 * @param <T> the type of the context value
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class EmptyRouter<T> implements Router<T> {

    /**
     * A reusable singleton instance of {@code EmptyRouter}.
     */
    public static final Router INSTANCE = new EmptyRouter();

    /**
     * Returns a typed singleton instance of an empty router.
     *
     * @param <T> the type of the context value
     * @return the empty router instance
     */
    public static <T> Router<T> empty() {
        return INSTANCE;
    }

    @Override
    public PathContext<T> process(String path, Supplier<Iterable<String>> supplier) {
        return null;
    }

    @Override
    public PathContext<T> process(String path) {
        return null;
    }
}
