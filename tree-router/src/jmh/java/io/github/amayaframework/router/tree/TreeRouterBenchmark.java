package io.github.amayaframework.router.tree;

import io.github.amayaframework.path.Path;
import io.github.amayaframework.path.parser.PathParsers;
import io.github.amayaframework.router.Router;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
public class TreeRouterBenchmark {
    private static final int ROUTE_COUNT = 200;
    private static final int ACTIVE = 100;

    private static final List<String> ROUTES = prepareRoutes();
    private static final Router<?> ROUTER = prepareRouter();

    private static List<String> prepareRoutes() {
        var routes = new ArrayList<String>(ROUTE_COUNT);
        for (int i = 0; i < ROUTE_COUNT; i++) {
            // Смешиваем разные глубины
            switch (i % 4) {
                case 0:
                    routes.add("/seg" + i + "/*");
                    break;
                case 1:
                    routes.add("/seg" + i + "/a/*");
                    break;
                case 2:
                    routes.add("/seg" + i + "/a/b/*");
                    break;
                case 3:
                    routes.add("/seg" + i + "/a/b/c/*");
                    break;
            }
        }
        return routes;
    }

    private static Router<?> prepareRouter() {
        var parser = PathParsers.createDefault();
        var paths = new HashMap<Path, String>();
        // только первые ACTIVE маршрутов реально зашьём
        for (int i = 0; i < ACTIVE; i++) {
            paths.put(parser.parse(ROUTES.get(i)), "handler-" + i);
        }
        return new TreeRouterFactory().create(paths);
    }

    private String validRoute;
    private String invalidRoute;
    private String mixedRoute;

    @Setup(Level.Iteration)
    public void setup() {
        var rnd = ThreadLocalRandom.current();

        // берём активный маршрут и делаем валидный путь (звёздочку заменяем на сегмент)
        var baseValid = ROUTES.get(rnd.nextInt(ACTIVE));
        validRoute = baseValid.replace("*", "val" + rnd.nextInt());

        // берём неактивный маршрут и делаем путь (он гарантированно не зашит)
        var baseInvalid = ROUTES.get(ACTIVE + rnd.nextInt(ROUTE_COUNT - ACTIVE));
        invalidRoute = baseInvalid.replace("*", "zzz");

        // берём активный маршрут, но добавляем лишние сегменты → невалидный
        mixedRoute = validRoute + "/extra/segments";
    }

    @Benchmark
    public void benchValidRoutes(Blackhole blackhole) {
        blackhole.consume(ROUTER.process(validRoute));
    }

    @Benchmark
    public void benchInvalidRoutes(Blackhole blackhole) {
        blackhole.consume(ROUTER.process(invalidRoute));
    }

    @Benchmark
    public void benchMixedRoutes(Blackhole blackhole) {
        blackhole.consume(ROUTER.process(mixedRoute));
    }
}
