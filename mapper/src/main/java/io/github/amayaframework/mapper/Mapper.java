package io.github.amayaframework.mapper;

/**
 *
 */
public interface Mapper {

    /**
     * @param path
     * @param segments
     * @return
     */
    String map(String path, Iterable<String> segments);

    /**
     * @param path
     * @return
     */
    String map(String path);
}
