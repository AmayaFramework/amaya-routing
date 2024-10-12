package io.github.amayaframework.router;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public final class StaticRouterTest {
    private static final RouterFactory STATIC_FACTORY = new StaticRouterFactory();

    @Test
    public void testStatic() {
        var paths = Map.of(
                TestUtil.parse("/s1"), "s1",
                TestUtil.parse("/s2"), "s2",
                TestUtil.parse("/a/b/c/s3"), "s3"
        );
        var router = STATIC_FACTORY.create(paths);
        var c1 = router.process("/s1");
        assertNotNull(c1);
        assertEquals("s1", c1.value);
        var c2 = router.process("/s2");
        assertNotNull(c2);
        assertEquals("s2", c2.value);
        var c3 = router.process("/a/b/c/s3");
        assertNotNull(c3);
        assertEquals("s3", c3.value);
        // s1
        assertEquals(c1, router.process("s1"));
        assertEquals(c1, router.process("s1/"));
        assertEquals(c1, router.process("/s1/"));
        // s2
        assertEquals(c2, router.process("s2"));
        assertEquals(c2, router.process("s2/"));
        assertEquals(c2, router.process("/s2/"));
        // s3
        assertEquals(c3, router.process("a/b/c/s3"));
        assertEquals(c3, router.process("a/b/c/s3/"));
        assertEquals(c3, router.process("/a/b/c/s3/"));
    }

    @Test
    public void testThrowOnDynamic() {
        var paths = Map.of(
                TestUtil.parse("/*"), ""
        );
        assertThrows(IllegalArgumentException.class, () -> STATIC_FACTORY.create(paths));
    }
}
