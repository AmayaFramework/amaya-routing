package io.github.amayaframework.fsm;

import com.github.romanqed.jsm.bytecode.BytecodeMachineFactory;
import io.github.amayaframework.path.Path;
import io.github.amayaframework.router.RouterFactory;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals("c1", c1.getValue());
        var c2 = router.process("/BBBB/_");
        Assertions.assertEquals("c2", c2.getValue());
        var c3 = router.process("/AaBB/_");
        Assertions.assertEquals("c3", c3.getValue());
        var c4 = router.process("/BBAa/_");
        Assertions.assertEquals("c4", c4.getValue());
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
        Assertions.assertEquals("s1", c1.getValue());
        var c2 = router.process("/s2");
        assertNotNull(c2);
        Assertions.assertEquals("s2", c2.getValue());
        var c3 = router.process("/a/b/c/s3");
        assertNotNull(c3);
        Assertions.assertEquals("s3", c3.getValue());
        // s1
        Assertions.assertEquals(c1, router.process("s1"));
        Assertions.assertEquals(c1, router.process("s1/"));
        Assertions.assertEquals(c1, router.process("/s1/"));
        // s2
        Assertions.assertEquals(c2, router.process("s2"));
        Assertions.assertEquals(c2, router.process("s2/"));
        Assertions.assertEquals(c2, router.process("/s2/"));
        // s3
        Assertions.assertEquals(c3, router.process("a/b/c/s3"));
        Assertions.assertEquals(c3, router.process("a/b/c/s3/"));
        Assertions.assertEquals(c3, router.process("/a/b/c/s3/"));
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
        Assertions.assertEquals("/a/*", ca.getValue());
        var ca1 = router.process("/a/_/1");
        Assertions.assertEquals("/a/*/1", ca1.getValue());
        var ca11 = router.process("/a/_/1/_");
        Assertions.assertEquals("/a/*/1/*", ca11.getValue());
        var ca2 = router.process("/a/_/2");
        Assertions.assertEquals("/a/*/2", ca2.getValue());
        var ca21 = router.process("/a/_/2/_");
        Assertions.assertEquals("/a/*/2/*", ca21.getValue());
        var ca3 = router.process("/a/_/3");
        Assertions.assertEquals("/a/*/3", ca3.getValue());
        var ca31 = router.process("/a/_/3/_");
        Assertions.assertEquals("/a/*/3/*", ca31.getValue());
        // b
        var cb = router.process("/b/_");
        Assertions.assertEquals("/b/*", cb.getValue());
        var cb1 = router.process("/b/_/1");
        Assertions.assertEquals("/b/*/1", cb1.getValue());
        var cb11 = router.process("/b/_/1/_");
        Assertions.assertEquals("/b/*/1/*", cb11.getValue());
        var cb2 = router.process("/b/_/2");
        Assertions.assertEquals("/b/*/2", cb2.getValue());
        var cb21 = router.process("/b/_/2/_");
        Assertions.assertEquals("/b/*/2/*", cb21.getValue());
        var cb3 = router.process("/b/_/3");
        Assertions.assertEquals("/b/*/3", cb3.getValue());
        var cb31 = router.process("/b/_/3/_");
        Assertions.assertEquals("/b/*/3/*", cb31.getValue());
        // a
        Assertions.assertEquals(ca, router.process("/a/aaaa"));
        Assertions.assertEquals(ca1, router.process("/a/segment/1"));
        Assertions.assertEquals(ca11, router.process("/a/seg/1/seg"));
        Assertions.assertEquals(ca2, router.process("/a/ss/2"));
        Assertions.assertEquals(ca21, router.process("/a/dd/2/gfg"));
        Assertions.assertEquals(ca3, router.process("/a/lo/3"));
        Assertions.assertEquals(ca31, router.process("/a/qwerty/3/bebra"));
        // b
        Assertions.assertEquals(cb, router.process("/b/bbbb"));
        Assertions.assertEquals(cb1, router.process("/b/segm/1"));
        Assertions.assertEquals(cb11, router.process("/b/seg/1/seq"));
        Assertions.assertEquals(cb2, router.process("/b/lok/2"));
        Assertions.assertEquals(cb21, router.process("/b/pp/2/123"));
        Assertions.assertEquals(cb3, router.process("/b/xd/3"));
        Assertions.assertEquals(cb31, router.process("/b/lol/3/kek"));
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
        Assertions.assertEquals("/s1/*/s2/*", c1.getValue());
        var c2 = router.process("/s1/o1/s2/_");
        Assertions.assertEquals("/s1/o1/s2/*", c2.getValue());
        var c3 = router.process("/s1/_/s2/o2");
        Assertions.assertEquals("/s1/*/s2/o2", c3.getValue());
        Assertions.assertEquals(c1, router.process("/s1/v1/s2/v2"));
        Assertions.assertEquals(c2, router.process("/s1/o1/s2/v2"));
        Assertions.assertEquals(c3, router.process("/s1/v1/s2/o2"));
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
        Assertions.assertEquals("/static", c1.getValue());
        var c2 = router.process("/dynamic/_");
        Assertions.assertEquals("/dynamic/*", c2.getValue());
        var c3 = router.process("/dynamic/override");
        Assertions.assertEquals("/dynamic/override", c3.getValue());
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
        Assertions.assertEquals("/a/*", c1.getValue());
        var c2 = router.process("/a/_/s");
        Assertions.assertEquals("/a/*/s", c2.getValue());
        var c3 = router.process("/a/override");
        Assertions.assertEquals("/a/override", c3.getValue());
        var c4 = router.process("/a/override/s");
        assertNull(c4);
    }
}
