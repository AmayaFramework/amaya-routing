package io.github.amayaframework.routing;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class StandardUrlDecoder implements UrlDecoder {
    private final Charset charset;

    public StandardUrlDecoder(Charset charset) {
        this.charset = charset;
    }

    public StandardUrlDecoder() {
        this.charset = StandardCharsets.UTF_8;
    }

    @Override
    public String decode(String source) {
        return URLDecoder.decode(source, charset);
    }
}
