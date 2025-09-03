package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Exceptions;
import io.github.amayaframework.router.RouterFactory;

final class LookupUtil {
    // Tree router
    private static final String TREE_FACTORY = "io.github.amayaframework.router.tree.TreeRouterFactory";
    // Fsm router
    private static final String FSM_FACTORY = "io.github.amayaframework.router.fsm.MachineRouterFactory";

    private LookupUtil() {
    }

    static Class<?> loadClass(String name) {
        var loader = Thread.currentThread().getContextClassLoader();
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T createInstance(Class<?> clazz) {
        try {
            return (T) clazz.getConstructor().newInstance((Object[]) null);
        } catch (Throwable e) {
            Exceptions.throwAny(e);
            return null;
        }
    }

    static RouterFactory lookupRouterFactory() {
        var clazz = loadClass(FSM_FACTORY);
        if (clazz != null) {
            return createInstance(clazz);
        }
        clazz = loadClass(TREE_FACTORY);
        if (clazz != null) {
            return createInstance(clazz);
        }
        return null;
    }
}
