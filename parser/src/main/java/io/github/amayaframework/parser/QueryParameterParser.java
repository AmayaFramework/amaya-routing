package io.github.amayaframework.parser;

import io.github.amayaframework.path.QueryParameter;

public interface QueryParameterParser {

    QueryParameter parse(String parameter);
}
