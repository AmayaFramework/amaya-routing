package io.github.amayaframework.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class TypedPathParserTest {
    private static final TypedPathParameterParser PARSER = new TypedPathParameterParser(':');

    @Test
    public void testUntyped() {
        assertEquals("a", PARSER.parse("a", 0).getName());
        assertEquals("b", PARSER.parse("b", 0).getName());
        assertEquals("name", PARSER.parse("name", 0).getName());
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse("", 0));
    }

    @Test
    public void testTyped() {
        var p1 = PARSER.parse("a:t1", 0);
        var p2 = PARSER.parse("a:", 0);
        assertEquals("a", p1.getName());
        assertEquals("t1", p1.getType());
        assertEquals("a", p2.getName());
        assertNull(p2.getType());
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse(":t1", 0));
    }
}
