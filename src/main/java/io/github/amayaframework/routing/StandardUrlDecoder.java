package io.github.amayaframework.routing;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A standard implementation of {@link UrlDecoder} based on {@link URLDecoder}.
 * <p>
 * This decoder applies percent-decoding to URL-encoded strings using the configured
 * {@link Charset}. By default, {@link StandardCharsets#UTF_8} is used.
 * <p>
 * It is a lightweight utility class intended for decoding path segments,
 * query parameters, and other URL components.
 */
public final class StandardUrlDecoder implements UrlDecoder {
    private final Charset charset;

    /**
     * Creates a new URL decoder with the specified character set.
     *
     * @param charset the charset to use for decoding (must not be {@code null})
     */
    public StandardUrlDecoder(Charset charset) {
        this.charset = charset;
    }

    /**
     * Creates a new URL decoder using {@link StandardCharsets#UTF_8}.
     */
    public StandardUrlDecoder() {
        this.charset = StandardCharsets.UTF_8;
    }

    @Override
    public String decode(String source) {
        return URLDecoder.decode(source, charset);
    }
}
