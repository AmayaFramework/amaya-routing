# amaya-routing [![amaya-routing](https://img.shields.io/maven-central/v/io.github.amayaframework/amaya-jetty?color=blue)](https://repo1.maven.org/maven2/io/github/amayaframework/amaya-routing)

A module of the amaya framework that implements routing of http paths
and processing of path and query parameters.

## Getting Started

To install it, you will need:

* Java 11+
* Maven/Gradle
* Amaya Core or set of core modules

### Features

* Fast route processing
* Static routing
* Dynamic routing
* Path parameter parsing
* Query parameter parsing

## Installing

### Gradle dependency

```Groovy
dependencies {
    implementation group: 'com.github.romanqed', name: 'amaya-core', version: '2.0.0'
    implementation group: 'com.github.romanqed', name: 'amaya-routing', version: '1.0.0'
}
```

### Maven dependency

```
<dependency>
    <groupId>io.github.amayaframework</groupId>
    <artifactId>amaya-routing</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Examples

### Hello world

```Java
import io.github.amayaframework.core.WebBuilders;
import io.github.amayaframework.http.HttpMethod;

public final class Main {
    public static void main(String[] args) throws Throwable {
        var cfg = RoutingConfigurers.create();
        // Configure routes
        var paths = cfg.getPathSet();
        paths.set(HttpMethod.GET, "/hello", ctx -> {
            ctx.getResponse().getWriter().write("Hello from amaya");
        });
        // Configure app
        var builder = WebBuilders.create();
        var app = builder
                .setServerFactory(/* your web server factory here */)
                .configureApplication(cfg)
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
* Black magic

## Authors

* **[RomanQed](https://github.com/RomanQed)** - *Main work*

See also the list of [contributors](https://github.com/AmayaFramework/amaya-jetty/contributors)
who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
