package io.github.amayaframework.router;

import io.github.amayaframework.tokenize.Tokenizers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestUtil {
    private TestUtil() {
    }

    static Path parse(String p) {
        var index = p.indexOf('?');
        if (index < 0) {
            return parsePath(p);
        }
        var pp = p.substring(0, index);
        var qp = p.substring(index + 1);
        var qs = parseQuery(qp);
        var ret = parsePath(pp);
        ret.data.queryParameters = qs;
        return ret;
    }

    static Path parsePath(String p) {
        var data = new PathData();
        data.pathParameters = new ArrayList<>();
        var segments = new ArrayList<String>();
        var norm = new StringBuilder();
        var tokens = Tokenizers.split(p, "/");
        var i = 0;
        var dynamic = false;
        for (var segment : tokens) {
            if (segment.equals("*")) {
                dynamic = true;
                norm.append("/*");
                segments.add(null);
                continue;
            }
            var temp = parsePathParam(segment, i++);
            if (temp == null) {
                norm.append('/').append(segment);
                segments.add(segment);
                continue;
            }
            dynamic = true;
            segments.add(null);
            norm.append("/*");
            data.pathParameters.add(temp);
        }
        var ret = new Path(norm.toString(), segments, dynamic);
        ret.data = data;
        return ret;
    }

    static List<QueryParameter> parseQuery(String q) {
        var qs = Tokenizers.split(q, "&");
        var ret = new HashMap<String, QueryParameter>();
        for (var p : qs) {
            var t = parseQueryParam(p);
            if (t == null) {
                continue;
            }
            ret.put(t.name, t);
        }
        return new ArrayList<>(ret.values());
    }

    static boolean noBrackets(String s) {
        final var brackets = Map.of(
                '{', '}',
                '(', ')',
                '[', ']'
        );
        var length = s.length();
        if (length < 2) {
            return true;
        }
        var first = s.charAt(0);
        var last = s.charAt(length - 1);
        var found = brackets.get(first);
        return found == null || found != last;
    }

    static PathParameter parsePathParam(String param, int i) {
        if (noBrackets(param)) {
            return null;
        }
        var inner = param
                .substring(1, param.length() - 1)
                .strip();
        if (inner.isEmpty() || inner.equals(":")) {
            return new PathParameter(null, i, null);
        }
        var index = inner.indexOf(':');
        if (index < 0) {
            return new PathParameter(inner, i, null);
        }
        var name = inner.substring(0, index);
        var type = inner.substring(index + 1);
        return new PathParameter(name, i, type);
    }

    static QP parseReq(String s) {
        var ln = s.length();
        var last = s.charAt(ln - 1);
        if (last == '?') {
            return new QP(s.substring(0, ln - 1), false);
        }
        if (last == '!') {
            return new QP(s.substring(0, ln - 1), true);
        }
        return new QP(s, null);
    }

    static QueryParameter parseQueryParam(String param) {
        if (noBrackets(param)) {
            return null;
        }
        var inner = param
                .substring(1, param.length() - 1)
                .strip();
        if (inner.isEmpty()) {
            return null;
        }
        var index = inner.indexOf(':');
        if (index < 0) {
            var rq = parseReq(inner);
            return new QueryParameter(rq.name, rq.req, null);
        }
        var name = inner.substring(0, index);
        var type = inner.substring(index + 1);
        var rq = parseReq(name);
        return new QueryParameter(rq.name, rq.req, type);
    }

    static final class QP {
        final String name;
        final Boolean req;

        QP(String name, Boolean req) {
            this.name = name;
            this.req = req;
        }
    }
}
