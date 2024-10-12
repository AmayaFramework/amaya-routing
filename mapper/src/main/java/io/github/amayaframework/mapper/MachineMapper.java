package io.github.amayaframework.mapper;

import com.github.romanqed.jsm.StateMachine;
import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;

final class MachineMapper implements Mapper {
    private final StateMachine<?, String> machine;
    private final Map<Long, String> dynamics;
    private final Tokenizer tokenizer;

    MachineMapper(StateMachine<?, String> machine, Map<Long, String> dynamics, Tokenizer tokenizer) {
        this.machine = machine;
        this.dynamics = dynamics;
        this.tokenizer = tokenizer;
    }

    @Override
    public String map(String path, Iterable<String> segments) {
        var hash = machine.stamp(segments);
        return dynamics.get(hash);
    }

    @Override
    public String map(String path) {
        return map(path, tokenizer.tokenize(path, "/"));
    }
}
