package io.github.amayaframework.parser;

import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.Map;

/**
 * Utility class that provides methods for creating instances of
 * {@link PathParser} with various configurations. This class contains
 * static methods that simplify the creation of path parsers
 * considering different delimiters and brackets.
 */
public final class PathParsers {

    /**
     * The character used for separating types in parameter declarations.
     */
    public static final char TYPE_DELIM = ':';

    private PathParsers() {
    }

    /**
     * Creates an instance of {@link PathParser} with the specified tokenizer,
     * bracket map, and delimiter.
     *
     * @param tokenizer the tokenizer to be used for parsing
     * @param brackets  a map that associates opening and closing brackets
     * @param delim     the delimiter character for paths
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets, char delim) {
        var pathParser = new TypedPathParameterParser(delim);
        var queryParser = new TypedQueryParameterParser(delim);
        return new BracketPathParser(tokenizer, brackets, pathParser, queryParser);
    }

    /**
     * Creates an instance of {@link PathParser} with the specified tokenizer, bracket map and
     * {@link PathParsers#TYPE_DELIM}.
     *
     * @param tokenizer the tokenizer to be used for parsing
     * @param brackets  a map that associates opening and closing brackets
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets) {
        return createDefault(tokenizer, brackets, TYPE_DELIM);
    }

    /**
     * Creates an instance of {@link PathParser} with the specified tokenizer, delimiter and
     * '&#123;&#40;&#91;' brackets.
     *
     * @param tokenizer the tokenizer to be used for parsing
     * @param delim     the delimiter character for paths
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(Tokenizer tokenizer, char delim) {
        var brackets = Map.of(
                '{', '}',
                '(', ')',
                '[', ']'
        );
        return createDefault(tokenizer, brackets, delim);
    }

    /**
     * Creates an instance of {@link PathParser} with the specified tokenizer, '&#123;&#40;&#91;' brackets
     * and {@link PathParsers#TYPE_DELIM}.
     *
     * @param tokenizer the tokenizer to be used for parsing
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(Tokenizer tokenizer) {
        return createDefault(tokenizer, TYPE_DELIM);
    }

    /**
     * Creates an instance of {@link PathParser} with the specified bracket map, {@link Tokenizers#PLAIN_TOKENIZER}
     * and {@link PathParsers#TYPE_DELIM}.
     *
     * @param brackets a map that associates opening and closing brackets
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(Map<Character, Character> brackets) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, brackets, TYPE_DELIM);
    }

    /**
     * Creates an instance of {@link PathParser} with the specified delimiter, {@link Tokenizers#PLAIN_TOKENIZER}
     * and '&#123;&#40;&#91;' brackets.
     *
     * @param delim the delimiter character for paths
     * @return an instance of {@link PathParser} with the specified configuration
     */
    public static PathParser createDefault(char delim) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, delim);
    }

    /**
     * Creates an instance of {@link PathParser} with {@link Tokenizers#PLAIN_TOKENIZER}, '&#123;&#40;&#91;' brackets
     * and {@link PathParsers#TYPE_DELIM}.
     *
     * @return an instance of {@link PathParser}
     */
    public static PathParser createDefault() {
        var brackets = Map.of(
                '{', '}',
                '(', ')',
                '[', ']'
        );
        return createDefault(Tokenizers.PLAIN_TOKENIZER, brackets, TYPE_DELIM);
    }
}
