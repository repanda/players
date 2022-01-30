package com.example.demo.infrastructure.eventbus;


import com.example.demo.api.EventBus;
import com.example.demo.infrastructure.logger.Logger;
import com.example.demo.domain.Message;

import java.util.Set;

/**
 * EventBus allows publish-subscribe communication between components.
 * It requires components to explicitly register to the bus using {@link #register(Object)} method.
 */
public class SimpleEventBus implements EventBus {

    private final Logger logger;
    private final Subscribers subscribers = new Subscribers();
    private Dispatcher dispatcher;

    /**
     * Creates a new EventBus instance.
     */
    public SimpleEventBus(Logger logger, Dispatcher dispatcher) {
        this.logger = logger;

        this.dispatcher = dispatcher;
        if (dispatcher instanceof PollDispatcher) {
            ((PollDispatcher) this.dispatcher).setSubscribers(subscribers);
        }
    }

    /**
     * Posts the given event to the event bus.
     */
    public void post(Object event) {
        Message message = (Message) event;
        logger.log(String.format("player: %s send message: %s", message.sender(), message.payload()));
        System.out.println(String.format("player: %s send message: %s", message.sender(), message.payload()));

        Set<Subscriber> subscribers = this.subscribers.getSubscribers(event);
        dispatcher.dispatch(event, subscribers);
    }

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object the object whose subscriber methods should be registered.
     */
    public void register(Object object) {
        subscribers.register(object);
    }

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     */
    public void unregister(Object object) {
        subscribers.unregister(object);
    }

    public boolean isEmpty() {
        return this.subscribers.isEmpty();
    }
}
