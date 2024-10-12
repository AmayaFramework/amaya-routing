package io.github.amayaframework.mapper;

import com.github.romanqed.jsm.bytecode.BytecodeMachineFactory;
import io.github.amayaframework.tokenize.Tokenizers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestMachineMapper {
    private static final MapperFactory FACTORY = new MachineMapperFactory(
            new BytecodeMachineFactory(),
            Tokenizers.PLAIN_TOKENIZER
    );

    @Test
    public void testDynamic() {
        var paths = List.of(
                // a
                "/a/*",
                // a 1
                "/a/*/1",
                "/a/*/1/*",
                // a 2
                "/a/*/2",
                "/a/*/2/*",
                // a 3
                "/a/*/3",
                "/a/*/3/*",
                // b
                "/b/*",
                // b 1
                "/b/*/1",
                "/b/*/1/*",
                // b 2
                "/b/*/2",
                "/b/*/2/*",
                // b 3
                "/b/*/3",
                "/b/*/3/*"
        );
        var mapper = FACTORY.create(paths);
        // a
        assertEquals("/a/*", mapper.map("/a/1"));
        // a 1
        assertEquals("/a/*/1", mapper.map("/a/v/1"));
        assertEquals("/a/*/1/*", mapper.map("/a/a/1/b"));
        // a 2
        assertEquals("/a/*/2", mapper.map("/a/f/2"));
        assertEquals("/a/*/2/*", mapper.map("/a/x/2/l"));
        // a 3
        assertEquals("/a/*/3", mapper.map("/a/f/3"));
        assertEquals("/a/*/3/*", mapper.map("/a/p/3/1"));
        // b
        assertEquals("/b/*", mapper.map("/b/1"));
        // b 1
        assertEquals("/b/*/1", mapper.map("/b/f/1"));
        assertEquals("/b/*/1/*", mapper.map("/b/d/1/0"));
        // b 2
        assertEquals("/b/*/2", mapper.map("/b/o/2"));
        assertEquals("/b/*/2/*", mapper.map("/b/e/2/q"));
        // b 3
        assertEquals("/b/*/3", mapper.map("/b/k/3"));
        assertEquals("/b/*/3/*", mapper.map("/b/k/3/a"));
    }

    @Test
    public void testOverrides() {
        var paths = List.of(
                "/s1/*/s2/*",
                "/s1/o1/s2/*",
                "/s1/*/s2/o2",
                "/s1/o1/s2/o2"
        );
        var mapper = FACTORY.create(paths);
        assertEquals("/s1/*/s2/*", mapper.map("/s1/v1/s2/v2"));
        assertEquals("/s1/o1/s2/*", mapper.map("/s1/o1/s2/x"));
        assertEquals("/s1/*/s2/o2", mapper.map("/s1/v/s2/o2"));
        assertEquals("/s1/o1/s2/o2", mapper.map("/s1/o1/s2/o2"));
    }
}
