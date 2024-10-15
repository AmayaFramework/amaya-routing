package io.github.amayaframework.parser;

import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.Map;

public final class PathParsers {
    public static final char TYPE_DELIM = ':';

    private PathParsers() {
    }

    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets, char delim) {
        var pathParser = new TypedPathParser(delim);
        var queryParser = new TypedQueryParser(delim);
        return new BracketPathParser(tokenizer, brackets, pathParser, queryParser);
    }

    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets) {
        return createDefault(tokenizer, brackets, TYPE_DELIM);
    }

    public static PathParser createDefault(Tokenizer tokenizer, char delim) {
        var brackets = Map.of(
                '{', '}',
                '(', ')',
                '[', ']'
        );
        return createDefault(tokenizer, brackets, delim);
    }

    public static PathParser createDefault(Tokenizer tokenizer) {
        return createDefault(tokenizer, TYPE_DELIM);
    }

    public static PathParser createDefault(Map<Character, Character> brackets) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, brackets, TYPE_DELIM);
    }

    public static PathParser createDefault(char delim) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, delim);
    }

    public static PathParser createDefault() {
        var brackets = Map.of(
                '{', '}',
                '(', ')',
                '[', ']'
        );
        return createDefault(Tokenizers.PLAIN_TOKENIZER, brackets, TYPE_DELIM);
    }
}
