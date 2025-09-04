package io.github.amayaframework.path.parser;

import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link PathParser} that uses bracketed declarations.
 * <p>
 * Example format:
 * <pre>{@code
 *   /users/{id:int}?active!:boolean
 * }</pre>
 * where <code>{}</code> are defined as valid brackets.
 * </p>
 */
public final class BracketPathParser extends AbstractPathParser {
    private static final String GENERIC = "*";
    private final Map<Character, Character> brackets;

    /**
     * Constructs a {@link BracketPathParser}.
     *
     * @param tokenizer   tokenizer for splitting paths
     * @param brackets    map of opening to closing brackets
     * @param pathParser  parser for path parameter declarations
     * @param queryParser parser for query parameter declarations
     */
    public BracketPathParser(Tokenizer tokenizer,
                             Map<Character, Character> brackets,
                             PathParameterParser pathParser,
                             QueryParameterParser queryParser) {
        super(
                Objects.requireNonNull(tokenizer),
                GENERIC,
                Objects.requireNonNull(pathParser),
                Objects.requireNonNull(queryParser)
        );
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
