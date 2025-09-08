package io.github.amayaframework.routing;

import com.github.romanqed.jconv.AsyncTask;
import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import io.github.amayaframework.http.HttpVersion;
import io.github.amayaframework.path.PathData;
import io.github.amayaframework.router.PathContext;
import io.github.amayaframework.router.Router;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class AbstractRoutingTaskTest {
    private AbstractRoutingTask task(Router<MethodMap> router, ParamParser parser) {
        return new AbstractRoutingTask(router, parser, true, null) {};
    }

    private HttpContext context(HttpRequest req, HttpResponse resp) {
        var ctx = mock(HttpContext.class);
        when(ctx.request()).thenReturn(req);
        when(ctx.response()).thenReturn(resp);
        return ctx;
    }

    private PathContext<MethodMap> pathContext(MethodMap map) {
        return new PathContext<>(new PathData(), map);
    }

    private Router router(PathContext<MethodMap> p) {
        return new Router() {
            @Override
            public PathContext process(String s, Supplier supplier) {
                return p;
            }

            @Override
            public PathContext process(String s) {
                return p;
            }
        };
    }


    @Test
    public void testRunReturns404WhenPathNotFound() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);
        var ctx = context(req, resp);

        task(EmptyRouter.INSTANCE, null).run(ctx, null);

        verify(resp).sendError(HttpCode.NOT_FOUND, "Path not found");
    }

    @Test
    public void testRunReturns405WhenMethodNotAllowed() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.POST);
        when(req.httpVersion()).thenReturn(HttpVersion.HTTP_1_1);

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, (SyncTask) ctx -> {
        });
        var ctx = context(req, resp);

        task(router(pathContext(map)), null).run(ctx, null);

        verify(resp).header(eq("Allow"), contains("OPTIONS"));
        verify(resp).sendError(HttpCode.METHOD_NOT_ALLOWED);
    }

    @Test
    public void testRunHandlesOptionsRequest() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.OPTIONS);

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, (SyncTask) c -> {
        });
        var ctx = context(req, resp);

        task(router(pathContext(map)), null).run(ctx, null);

        verify(resp).status(HttpCode.NO_CONTENT);
        verify(resp).header(eq("Allow"), contains("GET"));
    }

    @Test
    public void testRunHttp10Returns404InsteadOf405() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.POST);
        when(req.httpVersion()).thenReturn(HttpVersion.HTTP_1_0);

        var map = new ArrayMethodMap();
        var ctx = context(req, resp);

        task(router(pathContext(map)), null).run(ctx, null);

        verify(resp).sendError(HttpCode.NOT_FOUND, "Method POST not allowed");
    }

    @Test
    public void testRunExecutesHandlerWithoutParser() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.GET);

        var executed = new boolean[1];
        var handler = (SyncTask<HttpContext>) c -> executed[0] = true;

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, handler);

        var ctx = context(req, resp);

        task(router(pathContext(map)), null).run(ctx, null);

        assertTrue(executed[0]);
    }

    @Test
    public void testRunExecutesHandlerWithParser() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.GET);

        var executed = new boolean[1];
        var handler = (SyncTask<HttpContext>) c -> executed[0] = true;

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, handler);

        var parser = mock(ParamParser.class);
        var ctx = context(req, resp);

        task(router(pathContext(map)), parser).run(ctx, null);

        verify(parser).process(eq(req), any());
        assertTrue(executed[0]);
    }

    @Test
    public void testRunReturns400WhenParserFails() throws Throwable {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.GET);

        var handler = (SyncTask<HttpContext>) c -> { throw new AssertionError("Should not run"); };

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, handler);

        var parser = mock(ParamParser.class);
        doThrow(new IllegalParamException("bad param")).when(parser).process(eq(req), any());

        var ctx = context(req, resp);

        task(router(pathContext(map)), parser).run(ctx, null);

        verify(resp).sendError(HttpCode.BAD_REQUEST, "bad param");
    }

    @Test
    public void testRunAsyncReturns404WhenPathNotFound() throws IOException {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);
        var ctx = context(req, resp);

        var future = task(EmptyRouter.INSTANCE, null).runAsync(ctx, null);

        // Должен вернуть completed future
        assertTrue(future.isDone() && !future.isCompletedExceptionally());
        verify(resp).sendError(HttpCode.NOT_FOUND, "Path not found");
    }

    @Test
    public void testRunAsyncExecutesHandler() {
        var req = mock(HttpRequest.class);
        var resp = mock(HttpResponse.class);

        when(req.method()).thenReturn(HttpMethod.GET);

        var executed = new boolean[1];
        var handler = (AsyncTask) ctx -> {
            executed[0] = true;
            return CompletableFuture.completedFuture(null);
        };

        var map = new ArrayMethodMap();
        map.put(HttpMethod.GET, handler);

        var ctx = context(req, resp);
        var future = task(router(pathContext(map)), null).runAsync(ctx, null);

        // Должен выполниться handler
        future.join();
        assertTrue(executed[0]);
    }
}
