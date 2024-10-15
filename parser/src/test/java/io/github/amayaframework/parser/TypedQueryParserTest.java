package io.github.amayaframework.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class TypedQueryParserTest {
    private static final TypedQueryParser PARSER = new TypedQueryParser(':');

    @Test
    public void testTyped() {
        var p1 = PARSER.parse("a:t1");
        var p2 = PARSER.parse("a:");
        assertEquals("a", p1.getName());
        assertEquals("t1", p1.getType());
        assertEquals("a", p2.getName());
        assertNull(p2.getType());
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse(":t1"));
    }

    @Test
    public void testUntyped() {
        assertEquals("a", PARSER.parse("a").getName());
        assertEquals("b", PARSER.parse("b").getName());
        assertEquals("name", PARSER.parse("name").getName());
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse(""));
    }

    @Test
    public void testRequired() {
        // Typed
        var t1 = PARSER.parse("a!:t");
        assertEquals("a", t1.getName());
        assertEquals("t", t1.getType());
        assertTrue(t1.isRequired());
        var t2 = PARSER.parse("a?:t");
        assertEquals("a", t2.getName());
        assertEquals("t", t2.getType());
        assertFalse(t2.isRequired());
        var t3 = PARSER.parse("a:t");
        assertEquals("a", t3.getName());
        assertEquals("t", t3.getType());
        assertNull(t3.isRequired());
        // Untyped
        var u1 = PARSER.parse("a!");
        assertEquals("a", u1.getName());
        assertTrue(u1.isRequired());
        var u2 = PARSER.parse("a?");
        assertEquals("a", u2.getName());
        assertFalse(u2.isRequired());
        var u3 = PARSER.parse("a");
        assertEquals("a", u3.getName());
        assertNull(u3.isRequired());
        // Bad params
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse("!:t"));
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse("?:t"));
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse(":t"));
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse("!"));
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse("?"));
        assertThrows(IllegalArgumentException.class, () -> PARSER.parse(""));
    }
}
