package io.github.amayaframework.routing;

import com.github.romanqed.jfunc.Runnable1;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.http.HttpMethod;

/**
 *
 */
public interface PathSet {

    /**
     *
     * @param method
     * @param path
     * @return
     */
    Runnable1<HttpContext> get(HttpMethod method, String path);

    /**
     *
     * @param method
     * @param path
     * @param handler
     */
    void set(HttpMethod method, String path, Runnable1<HttpContext> handler);

    /**
     *
     * @param method
     * @param path
     */
    void remove(HttpMethod method, String path);

    /**
     *
     * @return
     */
    boolean isEmpty();

    /**
     *
     */
    void clear();

    /**
     *
     * @param path
     * @param handler
     */
    default void setGet(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.GET, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setHead(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.HEAD, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setPost(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.POST, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setPut(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.PUT, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setDelete(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.DELETE, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setConnect(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.CONNECT, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setOptions(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.OPTIONS, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setTrace(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.TRACE, path, handler);
    }

    /**
     *
     * @param path
     * @param handler
     */
    default void setPatch(String path, Runnable1<HttpContext> handler) {
        set(HttpMethod.PATCH, path, handler);
    }
}
