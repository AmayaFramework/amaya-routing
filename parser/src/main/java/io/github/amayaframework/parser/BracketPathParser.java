package io.github.amayaframework.parser;

import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.Objects;

public final class BracketPathParser extends AbstractPathParser {
    private static final String GENERIC = "*";
    private final Map<Character, Character> brackets;

    public BracketPathParser(Tokenizer tokenizer,
                             Map<Character, Character> brackets,
                             PathParameterParser pathParser,
                             QueryParameterParser queryParser) {
        super(tokenizer, GENERIC, pathParser, queryParser);
        this.brackets = Objects.requireNonNull(brackets);
    }

    @Override
    protected String unwrapPathParameter(String parameter) {
        var length = parameter.length();
        if (length < 2) {
            return null;
        }
        var first = parameter.charAt(0);
        var last = parameter.charAt(length - 1);
        var found = brackets.get(first);
        if (found == null || found != last) {
            return null;
        }
        return parameter.substring(1, length - 1);
    }
}
