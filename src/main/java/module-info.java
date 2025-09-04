module amayaframework.routing {
    // Imports
    // Basic dependencies
    requires com.github.romanqed.jtype;
    requires com.github.romanqed.juni;
    requires amayaframework.tokenize;
    // Filters
    requires amayaframework.filter;
    // Path
    requires amayaframework.path;
    requires amayaframework.path.parser;
    // Router
    requires amayaframework.router;
    // Amaya modules
    requires amayaframework.options;
    requires amayaframework.web;
    // Exports
    exports io.github.amayaframework.routing;
}
