package io.github.amayaframework.router;

import com.github.romanqed.jsm.bytecode.BytecodeMachineFactory;
import io.github.amayaframework.path.Path;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public final class MachineRouterTest {
    private static final RouterFactory DYNAMIC_FACTORY = new MachineRouterFactory(new BytecodeMachineFactory());

    private static Map<Path, String> parse(List<String> paths) {
        var ret = new HashMap<Path, String>();
        for (var path : paths) {
            ret.put(TestUtil.parse(path), path);
        }
        return ret;
    }

    @Test
    public void testCollisions() {
        var paths = Map.of(
                TestUtil.parse("/AaAa/*"), "c1",
                TestUtil.parse("/BBBB/*"), "c2",
                TestUtil.parse("/AaBB/*"), "c3",
                TestUtil.parse("/BBAa/*"), "c4"
        );
        var router = DYNAMIC_FACTORY.create(paths);
        var c1 = router.process("/AaAa/_");
        assertEquals("c1", c1.value);
        var c2 = router.process("/BBBB/_");
        assertEquals("c2", c2.value);
        var c3 = router.process("/AaBB/_");
        assertEquals("c3", c3.value);
        var c4 = router.process("/BBAa/_");
        assertEquals("c4", c4.value);
    }

    @Test
    public void testStatic() {
        var paths = Map.of(
                TestUtil.parse("/s1"), "s1",
                TestUtil.parse("/s2"), "s2",
                TestUtil.parse("/a/b/c/s3"), "s3"
        );
        var router = DYNAMIC_FACTORY.create(paths);
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
        assertEquals("/a/*", ca.value);
        var ca1 = router.process("/a/_/1");
        assertEquals("/a/*/1", ca1.value);
        var ca11 = router.process("/a/_/1/_");
        assertEquals("/a/*/1/*", ca11.value);
        var ca2 = router.process("/a/_/2");
        assertEquals("/a/*/2", ca2.value);
        var ca21 = router.process("/a/_/2/_");
        assertEquals("/a/*/2/*", ca21.value);
        var ca3 = router.process("/a/_/3");
        assertEquals("/a/*/3", ca3.value);
        var ca31 = router.process("/a/_/3/_");
        assertEquals("/a/*/3/*", ca31.value);
        // b
        var cb = router.process("/b/_");
        assertEquals("/b/*", cb.value);
        var cb1 = router.process("/b/_/1");
        assertEquals("/b/*/1", cb1.value);
        var cb11 = router.process("/b/_/1/_");
        assertEquals("/b/*/1/*", cb11.value);
        var cb2 = router.process("/b/_/2");
        assertEquals("/b/*/2", cb2.value);
        var cb21 = router.process("/b/_/2/_");
        assertEquals("/b/*/2/*", cb21.value);
        var cb3 = router.process("/b/_/3");
        assertEquals("/b/*/3", cb3.value);
        var cb31 = router.process("/b/_/3/_");
        assertEquals("/b/*/3/*", cb31.value);
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
        assertEquals("/s1/*/s2/*", c1.value);
        var c2 = router.process("/s1/o1/s2/_");
        assertEquals("/s1/o1/s2/*", c2.value);
        var c3 = router.process("/s1/_/s2/o2");
        assertEquals("/s1/*/s2/o2", c3.value);
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
        assertEquals("/static", c1.value);
        var c2 = router.process("/dynamic/_");
        assertEquals("/dynamic/*", c2.value);
        var c3 = router.process("/dynamic/override");
        assertEquals("/dynamic/override", c3.value);
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
        assertEquals("/a/*", c1.value);
        var c2 = router.process("/a/_/s");
        assertEquals("/a/*/s", c2.value);
        var c3 = router.process("/a/override");
        assertEquals("/a/override", c3.value);
        var c4 = router.process("/a/override/s");
        assertNull(c4);
    }
}
