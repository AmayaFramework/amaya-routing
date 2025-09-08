# amaya-routing [![amaya-routing](https://img.shields.io/maven-central/v/io.github.amayaframework/amaya-routing?color=blue)](https://repo1.maven.org/maven2/io/github/amayaframework/amaya-routing)

A module of the amaya framework that implements HTTP path routing and parsing of path and query parameters.

## Getting Started

To install it, you will need:

* Java 11+
* Maven/Gradle
* [amaya-core](https://github.com/AmayaFramework/amaya-core) or a set of core modules

### Features

* Fast and reliable route processing
* Static routing
* Dynamic routing with parameters
* Path parameter parsing with filters
* Query parameter parsing with filters

## Installing

### Gradle dependency

```groovy
dependencies {
    implementation group: 'io.github.amayaframework', name: 'amaya-core', version: '3.5.0'
    implementation group: 'io.github.amayaframework', name: 'amaya-routing', version: '2.0.0'
    // Default stable dynamic router implementation
    implementation group: 'io.github.amayaframework', name: 'amaya-tree-router', version: '2.0.0'
}
```

### Maven dependency

```xml
<dependencies>
    <dependency>
        <groupId>io.github.amayaframework</groupId>
        <artifactId>amaya-core</artifactId>
        <version>3.5.0</version>
    </dependency>
    <dependency>
        <groupId>io.github.amayaframework</groupId>
        <artifactId>amaya-routing</artifactId>
        <version>2.0.0</version>
    </dependency>
    <!-- Default stable dynamic router implementation -->
    <dependency>
        <groupId>io.github.amayaframework</groupId>
        <artifactId>amaya-tree-router</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

## Examples

### Hello world

```java
import io.github.amayaframework.core.WebBuilders;
import io.github.amayaframework.routing.Routing;

public final class Main {
    public static void main(String[] args) throws Throwable {
        var app = WebBuilders.create()
                .configureApplication(Routing.configurer(cfg ->
                        cfg.router(r -> r.get("/hello", ctx ->
                                ctx.response().writer().println("Hello from amaya!")
                        ))
                ))
                .withServerFactory(/* your web server factory here */)
                .build();
        app.bind(8080);
        app.run();
    }
}
```

### Dynamic routing with filters
```java
import io.github.amayaframework.core.WebBuilders;
import io.github.amayaframework.routing.Routing;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws Throwable {
        var app = WebBuilders.create()
                .configureApplication(Routing.configurer(cfg ->
                        cfg.router(r -> r
                                .get("/a/{p:int}", ctx -> {
                                    int p = ctx.request().pathParam("p");
                                    ctx.response().writer().println("/a/" + p);
                                })
                                .get("/a/1", ctx -> {
                                    ctx.response().writer().println("/a/1 (static)");
                                })
                                .get("/b/{p:positive}", ctx -> {
                                    int p = ctx.request().pathParam("p");
                                    ctx.response().writer().println("/b/" + p);
                                })
                                .get("/word/{w:word}", ctx -> {
                                    String w = ctx.request().pathParam("w");
                                    ctx.response().writer().println("/word/" + w);
                                })
                        ).paramParser(p -> {
                            p.filterSet().set("int", Integer::parseInt);
                            p.filterSet().set("positive", raw -> {
                                var val = Integer.parseInt(raw);
                                if (val < 0) throw new IllegalArgumentException("Value must be >= 0");
                                return val;
                            });
                            p.filterSet().set("word", raw -> {
                                raw = URLDecoder.decode(raw, StandardCharsets.UTF_8).strip();
                                if (raw.contains(" ")) throw new IllegalArgumentException("Value must be a single word");
                                return raw;
                            });
                        })
                ))
                .withServerFactory(/* your web server factory here */)
                .build();
        app.bind(8080);
        app.run();
    }
}
```

## Built With

* [Gradle](https://gradle.org) - Dependency management
* [jfunc](https://github.com/RomanQed/jfunc) - Basic functional interfaces
* [jsm](https://github.com/RomanQed/jsm) - Finite state machine jit compiler
* [amaya-core](https://github.com/AmayaFramework/amaya-core) - Various amaya modules

## Authors

* **[RomanQed](https://github.com/RomanQed)** - *Main work*

See also the list of [contributors](https://github.com/AmayaFramework/amaya-jetty/contributors)
who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
