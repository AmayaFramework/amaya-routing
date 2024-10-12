package io.github.amayaframework.router;

import com.github.romanqed.jsm.StateMachine;
import com.github.romanqed.jsm.StateMachineFactory;
import com.github.romanqed.jsm.model.MachineModelBuilder;
import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.*;

/**
 * Implementation of {@link RouterFactory} that uses state machines for dynamic routing.
 */
public final class MachineRouterFactory implements RouterFactory {
    private static final String INITIAL_STATE = "I";
    private static final String EXIT_STATE = "E";

    private final StateMachineFactory factory;
    private final Tokenizer tokenizer;

    /**
     * Constructs a {@link MachineRouterFactory} instance with given {@link StateMachineFactory} and {@link Tokenizer}.
     *
     * @param factory   the specified {@link StateMachineFactory} instance, must be non-null
     * @param tokenizer the specified {@link Tokenizer} instance, must be non-null
     */
    public MachineRouterFactory(StateMachineFactory factory, Tokenizer tokenizer) {
        this.factory = Objects.requireNonNull(factory);
        this.tokenizer = Objects.requireNonNull(tokenizer);
    }

    /**
     * Constructs a {@link MachineRouterFactory} instance with given {@link StateMachineFactory} and
     * {@link io.github.amayaframework.tokenize.PlainTokenizer}.
     *
     * @param factory the specified {@link StateMachineFactory} instance, must be non-null
     */
    public MachineRouterFactory(StateMachineFactory factory) {
        this.factory = Objects.requireNonNull(factory);
        this.tokenizer = Tokenizers.PLAIN_TOKENIZER;
    }

    private static void add(MachineModelBuilder<Object, String> builder, Object from, Object to, String value) {
        builder.addState(to);
        if (value == null) {
            builder.addTransition(from, to);
        } else {
            builder.addTransition(from, to, value);
        }
    }

    private static Object toPositioned(String segment, int position) {
        if (segment == null) {
            return position;
        }
        return segment + position;
    }

    private static void add(MachineModelBuilder<Object, String> builder, List<String> segments) {
        var first = segments.get(0);
        add(builder, INITIAL_STATE, toPositioned(first, 0), first);
        for (var i = 1; i < segments.size(); ++i) {
            var previous = toPositioned(segments.get(i - 1), i - 1);
            var current = segments.get(i);
            var positioned = toPositioned(current, i);
            add(builder, previous, positioned, current);
        }
    }

    private StateMachine<Object, String> createMachine(List<Path> paths) {
        var builder = new MachineModelBuilder<>(Object.class, String.class);
        builder.setInitState(INITIAL_STATE);
        builder.setExitState(EXIT_STATE);
        for (var path : paths) {
            add(builder, path.segments);
        }
        var model = builder.build();
        return factory.create(model);
    }

    @Override
    public <T> Router<T> create(Map<Path, T> paths) {
        var statics = new HashMap<String, PathContext<T>>();
        var dynamics = new LinkedList<Path>();
        for (var entry : paths.entrySet()) {
            var path = entry.getKey();
            if (path.dynamic) {
                dynamics.add(path);
                continue;
            }
            var context = new PathContext<>(path.data, entry.getValue());
            statics.put(path.path, context);
        }
        if (dynamics.isEmpty()) {
            return new MachineRouter<>(tokenizer, statics, null, null);
        }
        var machine = createMachine(dynamics);
        var dynamicMap = new HashMap<Long, PathContext<T>>();
        for (var path : dynamics) {
            var hash = machine.stamp(path.segments);
            var context = new PathContext<>(path.data, paths.get(path));
            dynamicMap.put(hash, context);
        }
        return new MachineRouter<>(tokenizer, statics, machine, dynamicMap);
    }
}
