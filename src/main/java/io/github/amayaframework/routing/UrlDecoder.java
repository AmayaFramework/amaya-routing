package io.github.amayaframework.routing;

/**
 * A component responsible for decoding URL-encoded strings.
 * <p>
 * Typical use cases include decoding path segments, query parameters,
 * or other parts of a URL that may contain percent-encoded characters.
 */
public interface UrlDecoder {

    /**
     * Decodes the given URL-encoded string into its plain form.
     *
     * @param source the encoded string, must not be {@code null}
     * @return the decoded string
     * @throws IllegalArgumentException if the source contains
     *                                  invalid or unsupported escape sequences
     */
    String decode(String source);
}
