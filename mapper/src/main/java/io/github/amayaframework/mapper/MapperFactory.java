package io.github.amayaframework.mapper;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface MapperFactory {

    /**
     * @param paths
     * @return
     */
    Mapper create(Map<String, List<String>> paths);

    /**
     * @param paths
     * @return
     */
    Mapper create(Iterable<String> paths);
}
