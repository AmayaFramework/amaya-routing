package io.github.amayaframework.routing;

import com.github.romanqed.juni.Uni;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.Router;

import java.util.function.Supplier;

public interface TaskRouter extends Router<MethodMap>, Uni {

    @Override
    PathContext<MethodMap> process(String path, Supplier<Iterable<String>> supplier);

    @Override
    PathContext<MethodMap> process(String path);

    boolean empty();

    @Override
    boolean isSync();

    @Override
    boolean isAsync();

    @Override
    boolean isUni();
}
