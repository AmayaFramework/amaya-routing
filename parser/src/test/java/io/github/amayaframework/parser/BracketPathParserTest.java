package io.github.amayaframework.parser;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class BracketPathParserTest {
    private static final PathParser PARSER = PathParsers.createDefault();

    @Test
    public void testEmpty() {
        var p1 = PARSER.parse("");
        var p2 = PARSER.parse("/");
        var p3 = PARSER.parse("/?a");
        var p4 = PARSER.parse("?a");
        assertEquals("/", p1.getPath());
        assertEquals("/", p2.getPath());
        assertEquals("/", p3.getPath());
        assertEquals("/", p4.getPath());
        assertTrue(p1.getSegments().isEmpty());
        assertTrue(p2.getSegments().isEmpty());
        assertTrue(p3.getSegments().isEmpty());
        assertTrue(p4.getSegments().isEmpty());
    }

    @Test
    public void testStatic() {
        var p1 = PARSER.parse("/s1/s2");
        var p2 = PARSER.parse("/s1");
        // p1
        assertFalse(p1.isDynamic());
        assertEquals("/s1/s2", p1.getPath());
        assertIterableEquals(List.of("s1", "s2"), p1.getSegments());
        // p2
        assertFalse(p2.isDynamic());
        assertEquals("/s1", p2.getPath());
        assertIterableEquals(List.of("s1"), p2.getSegments());
    }

    @Test
    public void testStaticWithQuery() {
        var p1 = PARSER.parse("/s1/s2?a&b");
        var p2 = PARSER.parse("/s1?q:t");
        // p1
        assertFalse(p1.isDynamic());
        assertEquals("/s1/s2", p1.getPath());
        assertIterableEquals(List.of("s1", "s2"), p1.getSegments());
        var qs1 = p1.getData().getQueryParameters();
        assertEquals("a", qs1.get(0).getName());
        assertEquals("b", qs1.get(1).getName());
        // p2
        assertFalse(p2.isDynamic());
        assertEquals("/s1", p2.getPath());
        assertIterableEquals(List.of("s1"), p2.getSegments());
        var qs2 = p2.getData().getQueryParameters();
        assertEquals("q", qs2.get(0).getName());
        assertEquals("t", qs2.get(0).getType());
    }

    @Test
    public void testGeneric() {
        var p1 = PARSER.parse("/s1/*");
        var p2 = PARSER.parse("/*");
        // p1
        assertTrue(p1.isDynamic());
        assertEquals("/s1/*", p1.getPath());
        assertIterableEquals(Arrays.asList("s1", null), p1.getSegments());
        // p2
        assertTrue(p2.isDynamic());
        assertEquals("/*", p2.getPath());
        assertIterableEquals(Collections.singletonList(null), p2.getSegments());
    }

    @Test
    public void testGenericWithQuery() {
        var p1 = PARSER.parse("/s1/*?a&b");
        var p2 = PARSER.parse("/*?q:t");
        // p1
        assertTrue(p1.isDynamic());
        assertEquals("/s1/*", p1.getPath());
        assertIterableEquals(Arrays.asList("s1", null), p1.getSegments());
        var qs1 = p1.getData().getQueryParameters();
        assertEquals("a", qs1.get(0).getName());
        assertEquals("b", qs1.get(1).getName());
        // p2
        assertTrue(p2.isDynamic());
        assertEquals("/*", p2.getPath());
        assertIterableEquals(Collections.singletonList(null), p2.getSegments());
        var qs2 = p2.getData().getQueryParameters();
        assertEquals("q", qs2.get(0).getName());
        assertEquals("t", qs2.get(0).getType());
    }

    @Test
    public void testDynamic() {
        var p1 = PARSER.parse("/s1/{p}");
        var p2 = PARSER.parse("/{p:t}");
        // p1
        assertTrue(p1.isDynamic());
        assertEquals("/s1/*", p1.getPath());
        assertIterableEquals(Arrays.asList("s1", null), p1.getSegments());
        var pp1 = p1.getData().getPathParameters();
        assertEquals("p", pp1.get(0).getName());
        // p2
        assertTrue(p2.isDynamic());
        assertEquals("/*", p2.getPath());
        assertIterableEquals(Collections.singletonList(null), p2.getSegments());
        var pp2 = p2.getData().getPathParameters();
        assertEquals("p", pp2.get(0).getName());
        assertEquals("t", pp2.get(0).getType());
    }

    @Test
    public void testDynamicWithQuery() {
        var p1 = PARSER.parse("/s1/{p}?a&b");
        var p2 = PARSER.parse("/{p:t}?q:t");
        // p1
        assertTrue(p1.isDynamic());
        assertEquals("/s1/*", p1.getPath());
        assertIterableEquals(Arrays.asList("s1", null), p1.getSegments());
        var pp1 = p1.getData().getPathParameters();
        assertEquals("p", pp1.get(0).getName());
        var qs1 = p1.getData().getQueryParameters();
        assertEquals("a", qs1.get(0).getName());
        assertEquals("b", qs1.get(1).getName());
        // p2
        assertTrue(p2.isDynamic());
        assertEquals("/*", p2.getPath());
        assertIterableEquals(Collections.singletonList(null), p2.getSegments());
        var pp2 = p2.getData().getPathParameters();
        assertEquals("p", pp2.get(0).getName());
        assertEquals("t", pp2.get(0).getType());
        var qs2 = p2.getData().getQueryParameters();
        assertEquals("q", qs2.get(0).getName());
        assertEquals("t", qs2.get(0).getType());
    }
}
