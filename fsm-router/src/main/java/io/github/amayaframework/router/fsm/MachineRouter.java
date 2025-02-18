package io.github.amayaframework.router.fsm;

import com.github.romanqed.jsm.StateMachine;
import io.github.amayaframework.router.AbstractRouter;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.PathUtil;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.function.Supplier;

final class MachineRouter<T> extends AbstractRouter<T> {
    private final Map<String, PathContext<T>> statics;
    private final StateMachine<?, String> machine;
    private final Map<Long, PathContext<T>> dynamics;

    MachineRouter(Tokenizer tokenizer,
                  Map<String, PathContext<T>> statics,
                  StateMachine<?, String> machine,
                  Map<Long, PathContext<T>> dynamics) {
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
        if (machine == null) {
            return null;
        }
        var hash = machine.stamp(supplier.get());
        if (hash < 0) {
            return null;
        }
        return dynamics.get(hash);
    }
}
