package io.github.amayaframework.path.parser;

import io.github.amayaframework.tokenize.Tokenizer;
import io.github.amayaframework.tokenize.Tokenizers;

import java.util.Map;

/**
 * Factory utility for creating {@link PathParser} instances
 * with common configurations.
 * <p>
 * Provides static helper methods that assemble parsers using
 * {@link TypedPathParameterParser}, {@link TypedQueryParameterParser},
 * and {@link BracketPathParser}, with optional customization
 * of tokenizers, type delimiters, and bracket sets.
 * </p>
 */
public final class PathParsers {

    /**
     * The default character used to separate names and types
     * in parameter declarations.
     */
    public static final char TYPE_DELIM = ':';

    private PathParsers() {
    }

    /**
     * Creates a {@link PathParser} with the specified tokenizer,
     * bracket map, and type delimiter.
     *
     * @param tokenizer the {@link Tokenizer} to use for splitting
     *                  path and query strings
     * @param brackets  a map associating opening to closing brackets
     *                  for parameter declarations (e.g. {@code { }}, {@code ( )})
     * @param delim     the type delimiter character
     * @return a configured {@link PathParser} instance
     */
    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets, char delim) {
        var pathParser = new TypedPathParameterParser(delim);
        var queryParser = new TypedQueryParameterParser(delim);
        return new BracketPathParser(tokenizer, brackets, pathParser, queryParser);
    }

    /**
     * Creates a {@link PathParser} with the specified tokenizer
     * and bracket map, using {@link #TYPE_DELIM} as delimiter.
     *
     * @param tokenizer the {@link Tokenizer} to use
     * @param brackets  a map of opening to closing brackets
     * @return a configured {@link PathParser} instance
     */
    public static PathParser createDefault(Tokenizer tokenizer, Map<Character, Character> brackets) {
        return createDefault(tokenizer, brackets, TYPE_DELIM);
    }

    /**
     * Creates a {@link PathParser} with the specified tokenizer and delimiter,
     * using the default brackets {@code { }, ( ), [ ]}.
     *
     * @param tokenizer the {@link Tokenizer} to use
     * @param delim     the type delimiter character
     * @return a configured {@link PathParser} instance
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
     * Creates a {@link PathParser} with the specified tokenizer,
     * default brackets {@code { }, ( ), [ ]}, and {@link #TYPE_DELIM}.
     *
     * @param tokenizer the {@link Tokenizer} to use
     * @return a configured {@link PathParser} instance
     */
    public static PathParser createDefault(Tokenizer tokenizer) {
        return createDefault(tokenizer, TYPE_DELIM);
    }

    /**
     * Creates a {@link PathParser} with the specified bracket map,
     * {@link Tokenizers#PLAIN_TOKENIZER}, and {@link #TYPE_DELIM}.
     *
     * @param brackets a map of opening to closing brackets
     * @return a configured {@link PathParser} instance
     */
    public static PathParser createDefault(Map<Character, Character> brackets) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, brackets, TYPE_DELIM);
    }

    /**
     * Creates a {@link PathParser} with the specified delimiter,
     * {@link Tokenizers#PLAIN_TOKENIZER}, and default brackets.
     *
     * @param delim the type delimiter character
     * @return a configured {@link PathParser} instance
     */
    public static PathParser createDefault(char delim) {
        return createDefault(Tokenizers.PLAIN_TOKENIZER, delim);
    }

    /**
     * Creates a {@link PathParser} with {@link Tokenizers#PLAIN_TOKENIZER},
     * default brackets {@code { }, ( ), [ ]}, and {@link #TYPE_DELIM}.
     *
     * @return a configured {@link PathParser} instance
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
