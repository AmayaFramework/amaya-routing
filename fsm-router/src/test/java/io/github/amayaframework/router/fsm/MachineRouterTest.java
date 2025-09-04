package io.github.amayaframework.router.fsm;

import com.github.romanqed.jsm.asm.AsmMachineFactory;
import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.parser.PathParser;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.RouterFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public final class MachineRouterTest {
    private static final PathParser PARSER = PathParsers.createDefault();
    private static final RouterFactory DYNAMIC_FACTORY = new MachineRouterFactory(new AsmMachineFactory());

    private static Map<Path, String> parse(List<String> paths) {
        var ret = new HashMap<Path, String>();
        for (var path : paths) {
            ret.put(PARSER.parse(path), path);
        }
        return ret;
    }

    @Test
    public void testEmptyRouter() {
        var router = DYNAMIC_FACTORY.create(Map.of());
        assertNull(router.process("/any"));
        assertNull(router.process(""));
        assertNull(router.process("/"));
    }

    @Test
    public void testRootPath() {
        var paths = parse(List.of("/"));
        var router = DYNAMIC_FACTORY.create(paths);
        var root = router.process("/");
        assertNotNull(root);
        assertEquals("/", root.getValue());
        assertEquals(root, router.process(""));
        assertEquals(root, router.process("//"));
    }

    @Test
    public void testMultipleWildcards() {
        var paths = parse(List.of(
                "/*",
                "/*/*",
                "/*/*/*"
        ));
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/a");
        assertEquals("/*", c1.getValue());
        var c2 = router.process("/a/b");
        assertEquals("/*/*", c2.getValue());
        var c3 = router.process("/a/b/c");
        assertEquals("/*/*/*", c3.getValue());
        assertNull(router.process("/a/b/c/d"));
    }

    @Test
    public void testCollisions() {
        var paths = Map.of(
                PARSER.parse("/AaAa/*"), "c1",
                PARSER.parse("/BBBB/*"), "c2",
                PARSER.parse("/AaBB/*"), "c3",
                PARSER.parse("/BBAa/*"), "c4"
        );
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/AaAa/_");
        assertEquals("c1", c1.getValue());
        var c2 = router.process("/BBBB/_");
        assertEquals("c2", c2.getValue());
        var c3 = router.process("/AaBB/_");
        assertEquals("c3", c3.getValue());
        var c4 = router.process("/BBAa/_");
        assertEquals("c4", c4.getValue());
    }

    @Test
    public void testStatic() {
        var paths = Map.of(
                PARSER.parse("/s1"), "s1",
                PARSER.parse("/s2"), "s2",
                PARSER.parse("/a/b/c/s3"), "s3"
        );
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/s1");
        assertNotNull(c1);
        assertEquals("s1", c1.getValue());
        var c2 = router.process("/s2");
        assertNotNull(c2);
        assertEquals("s2", c2.getValue());
        var c3 = router.process("/a/b/c/s3");
        assertNotNull(c3);
        assertEquals("s3", c3.getValue());
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
    public void testDynamic() {
        var paths = parse(List.of(
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
        ));
        var router = DYNAMIC_FACTORY.create(paths);
        // a
        var ca = router.process("/a/_");
        assertEquals("/a/*", ca.getValue());
        var ca1 = router.process("/a/_/1");
        assertEquals("/a/*/1", ca1.getValue());
        var ca11 = router.process("/a/_/1/_");
        assertEquals("/a/*/1/*", ca11.getValue());
        var ca2 = router.process("/a/_/2");
        assertEquals("/a/*/2", ca2.getValue());
        var ca21 = router.process("/a/_/2/_");
        assertEquals("/a/*/2/*", ca21.getValue());
        var ca3 = router.process("/a/_/3");
        assertEquals("/a/*/3", ca3.getValue());
        var ca31 = router.process("/a/_/3/_");
        assertEquals("/a/*/3/*", ca31.getValue());
        // b
        var cb = router.process("/b/_");
        assertEquals("/b/*", cb.getValue());
        var cb1 = router.process("/b/_/1");
        assertEquals("/b/*/1", cb1.getValue());
        var cb11 = router.process("/b/_/1/_");
        assertEquals("/b/*/1/*", cb11.getValue());
        var cb2 = router.process("/b/_/2");
        assertEquals("/b/*/2", cb2.getValue());
        var cb21 = router.process("/b/_/2/_");
        assertEquals("/b/*/2/*", cb21.getValue());
        var cb3 = router.process("/b/_/3");
        assertEquals("/b/*/3", cb3.getValue());
        var cb31 = router.process("/b/_/3/_");
        assertEquals("/b/*/3/*", cb31.getValue());
        // a
        assertEquals(ca, router.process("/a/aaaa"));
        assertEquals(ca1, router.process("/a/segment/1"));
        assertEquals(ca11, router.process("/a/seg/1/seg"));
        assertEquals(ca2, router.process("/a/ss/2"));
        assertEquals(ca21, router.process("/a/dd/2/gfg"));
        assertEquals(ca3, router.process("/a/lo/3"));
        assertEquals(ca31, router.process("/a/qwerty/3/bebra"));
        // b
        assertEquals(cb, router.process("/b/bbbb"));
        assertEquals(cb1, router.process("/b/segm/1"));
        assertEquals(cb11, router.process("/b/seg/1/seq"));
        assertEquals(cb2, router.process("/b/lok/2"));
        assertEquals(cb21, router.process("/b/pp/2/123"));
        assertEquals(cb3, router.process("/b/xd/3"));
        assertEquals(cb31, router.process("/b/lol/3/kek"));
    }

    @Test
    public void testOverrides() {
        var paths = parse(List.of(
                "/s1/*/s2/*",
                "/s1/o1/s2/*",
                "/s1/*/s2/o2"
        ));
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/s1/_/s2/_");
        assertEquals("/s1/*/s2/*", c1.getValue());
        var c2 = router.process("/s1/o1/s2/_");
        assertEquals("/s1/o1/s2/*", c2.getValue());
        var c3 = router.process("/s1/_/s2/o2");
        assertEquals("/s1/*/s2/o2", c3.getValue());
        assertEquals(c1, router.process("/s1/v1/s2/v2"));
        assertEquals(c2, router.process("/s1/o1/s2/v2"));
        assertEquals(c3, router.process("/s1/v1/s2/o2"));
    }

    @Test
    public void testMixed() {
        var paths = parse(List.of(
                "/static", // static
                "/dynamic/*", // dynamic
                "/dynamic/override" // static
        ));
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/static");
        assertEquals("/static", c1.getValue());
        var c2 = router.process("/dynamic/_");
        assertEquals("/dynamic/*", c2.getValue());
        var c3 = router.process("/dynamic/override");
        assertEquals("/dynamic/override", c3.getValue());
    }

    @Test
    public void testOverrideCutout() {
        var paths = parse(List.of(
                "/a/*",
                "/a/*/s",
                "/a/override"
        ));
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/a/_");
        assertEquals("/a/*", c1.getValue());
        var c2 = router.process("/a/_/s");
        assertEquals("/a/*/s", c2.getValue());
        var c3 = router.process("/a/override");
        assertEquals("/a/override", c3.getValue());
        var c4 = router.process("/a/override/s");
        assertNull(c4);
    }

    @Test
    public void testPrefixPaths() {
        var paths = parse(List.of("/a/b/c/*"));
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/a/b/c/_");
        assertEquals("/a/b/c/*", c1.getValue());
        var c2 = router.process("/a/b/c/d");
        assertEquals("/a/b/c/*", c2.getValue());
        var c3 = router.process("/a/b/c/_/e/f/g");
        assertNull(c3);
        var c4 = router.process("/a/b/c/d/e/f/g/h");
        assertNull(c4);
    }
}
