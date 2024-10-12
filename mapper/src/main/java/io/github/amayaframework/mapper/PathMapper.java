package io.github.amayaframework.mapper;

/**
 * An interface describing an abstract path mapper.
 * That is, a certain function that translates the path from the values of one set to another.
 * For example, let there be some dynamic path pattern: /a/*.
 * Then the mapper will be a function that turns all paths corresponding to the pattern into its pattern:
 * <br>
 * F_mapper =
 * &#123;
 * <br>
 * /a/1 -&gt; /a/*;
 * <br>
 * /a/2 -&gt; /a/*;
 * <br>
 * ...
 * <br>
 * /a/random_segment -&gt; /a/*;
 * <br>
 * &#125;
 */
public interface PathMapper {

    /**
     * Maps given path to its preimage.
     *
     * @param segments the specified path segments, must be non-null
     * @return the found path preimage or null
     */
    String map(Iterable<String> segments);

    /**
     * Maps given path to its preimage.
     *
     * @param path the specified path, must be non-null
     * @return the found path preimage or null
     */
    String map(String path);
}
