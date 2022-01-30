package com.example.demo.api;

/**
 * EventBus allows publish-subscribe communication between components.
 * It requires components to explicitly register to the bus using {@link #register(Object)} method.
 */
public interface EventBus {

    /**
     * Posts the given event to the event bus.
     */
    void post(Object event);

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object the object whose subscriber methods should be registered.
     */
    void register(Object object);

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     */
    void unregister(Object object);
}
