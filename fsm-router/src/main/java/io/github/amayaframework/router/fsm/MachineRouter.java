package io.github.amayaframework.router.fsm;

import com.github.romanqed.jsm.StateMachine;
import io.github.amayaframework.router.AbstractRouter;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.PathUtil;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link io.github.amayaframework.router.Router} implementation
 * based on a finite-state machine (FSM).
 * <p>
 * This router supports both static and dynamic paths:
 * <ul>
 *   <li>Static paths are resolved through a precomputed {@link Map} lookup</li>
 *   <li>Dynamic paths are resolved by evaluating the input against
 *       a {@link com.github.romanqed.jsm.StateMachine}</li>
 * </ul>
 * </p>
 *
 * @param <T> the type of the context value
 */
final class MachineRouter<T> extends AbstractRouter<T> {
    private final Map<String, PathContext<T>> statics;
    private final StateMachine<?, String> machine;
    private final LongMap<PathContext<T>> dynamics;

    MachineRouter(Tokenizer tokenizer,
                  Map<String, PathContext<T>> statics,
                  StateMachine<?, String> machine,
                  LongMap<PathContext<T>> dynamics) {
        super(tokenizer);
        this.statics = statics;
        this.machine = machine;
        this.dynamics = dynamics;
    }

    @Override
    public PathContext<T> process(String path, Supplier<Iterable<String>> supplier) {
        var found = statics.get(PathUtil.normalize(path));
        if (found != null) {
            return found;
        }
        var hash = machine.stamp(supplier.get());
        if (hash < 0) {
            return null;
        }
        return dynamics.get(hash);
    }
}
