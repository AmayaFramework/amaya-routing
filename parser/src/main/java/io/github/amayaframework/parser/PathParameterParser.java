package io.github.amayaframework.parser;

import io.github.amayaframework.path.PathParameter;

public interface PathParameterParser {

    PathParameter parse(String parameter, int index);
}
