package io.github.amayaframework.routing;

import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.path.PathData;

/**
 * TODO
 */
public interface ParamParser {

    /**
     * TODO
     * @param request
     * @param data
     * @return
     */
    String process(HttpRequest request, PathData data); // возвращает ошибку, результат парсинга пишется в request
}
