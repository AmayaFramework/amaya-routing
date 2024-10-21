package io.github.amayaframework.parser;

import io.github.amayaframework.tokenize.Tokenizer;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of {@link PathParser} that works with the following http path template:
 * <br>
 * /segment1/&lt;opening bracket&gt;path param declaration&lt;closing bracket&gt;?&lt;query param declaration&gt;
 */
public final class BracketPathParser extends AbstractPathParser {
    private static final String GENERIC = "*";
    private final Map<Character, Character> brackets;

    /**
     * Constructs a {@link BracketPathParser} instance with given tokenizer, bracket map,
     * path and query parameter parsers.
     *
     * @param tokenizer   the specified {@link Tokenizer}, must be not null
     * @param brackets    the specified bracket map, must be not null
     * @param pathParser  the specified {@link PathParameterParser}, must be not null
     * @param queryParser the specified {@link QueryParameterParser}, must be not null
     */
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
