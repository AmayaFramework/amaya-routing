package io.github.amayaframework.router;

import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class EmptyRouter<T> implements Router<T> {
    public static final Router INSTANCE = new EmptyRouter();

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
