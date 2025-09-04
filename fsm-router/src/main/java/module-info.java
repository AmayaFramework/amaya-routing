/**
 * Module providing finite state machine–based routing implementation for Amaya framework.
 *
 * <p>This module integrates {@link com.github.romanqed.jsm} state machines
 * into the Amaya routing system, enabling efficient matching of dynamic paths.
 * It exposes the {@link io.github.amayaframework.router.fsm} package,
 * which contains the {@link io.github.amayaframework.router.fsm.MachineRouterFactory}
 * and related classes.</p>
 */
module amayaframework.router.fsm {
    // Imports
    requires com.github.romanqed.jsm;
    requires amayaframework.path;
    requires amayaframework.router;
    requires amayaframework.tokenize;
    // Exports
    exports io.github.amayaframework.router.fsm;
}
