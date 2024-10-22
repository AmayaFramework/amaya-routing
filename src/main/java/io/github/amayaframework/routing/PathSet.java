package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

/**
 *
 */
public interface PathSet {

    /**
     * @param method
     * @param path
     * @return
     */
    Runnable1<HttpContext> get(HttpMethod method, String path);

    /**
     * @param method
     * @param path
     * @param handler
     */
    void set(HttpMethod method, String path, Runnable1<HttpContext> handler);

    /**
     * @param method
     * @param path
     */
    void remove(HttpMethod method, String path);

    /**
     * @return
     */
    boolean isEmpty();

    /**
     *
     */
    void clear();
}
