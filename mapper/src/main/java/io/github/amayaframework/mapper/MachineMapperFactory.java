package io.github.amayaframework.mapper;

import com.github.romanqed.jsm.StateMachine;
import com.github.romanqed.jsm.StateMachineFactory;
import com.github.romanqed.jsm.model.MachineModelBuilder;
import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.*;

/**
 *
 */
public final class MachineMapperFactory implements MapperFactory {
    private static final String INITIAL_STATE = "I";
    private static final String EXIT_STATE = "E";
    private final StateMachineFactory factory;
    private final Tokenizer tokenizer;

    /**
     *
     * @param factory
     * @param tokenizer
     */
    public MachineMapperFactory(StateMachineFactory factory, Tokenizer tokenizer) {
        this.factory = Objects.requireNonNull(factory);
        this.tokenizer = tokenizer;
    }

    /**
     *
     * @param factory
     */
    public MachineMapperFactory(StateMachineFactory factory) {
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

    private StateMachine<Object, String> createMachine(Collection<List<String>> batches) {
        var builder = new MachineModelBuilder<>(Object.class, String.class);
        builder.setInitState(INITIAL_STATE);
        builder.setExitState(EXIT_STATE);
        for (var batch : batches) {
            add(builder, batch);
        }
        var model = builder.build();
        return factory.create(model);
    }

    private Mapper innerCreate(Map<String, List<String>> paths) {
        var machine = createMachine(paths.values());
        var map = new HashMap<Long, String>();
        for (var entry : paths.entrySet()) {
            var hash = machine.stamp(entry.getValue());
            map.put(hash, entry.getKey());
        }
        return new MachineMapper(machine, map, tokenizer);
    }

    @Override
    public Mapper create(Map<String, List<String>> paths) {
        Objects.requireNonNull(paths);
        return innerCreate(paths);
    }

    @Override
    public Mapper create(Iterable<String> paths) {
        Objects.requireNonNull(paths);
        var map = new HashMap<String, List<String>>();
        for (var path : paths) {
            var split = tokenizer.tokenize(path, "/");
            var tokens = new ArrayList<String>();
            for (var token : split) {
                tokens.add(token.equals("*") ? null : token);
            }
            map.put(path, tokens);
        }
        return innerCreate(map);
    }
}
