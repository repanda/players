package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.EventBus;
import com.example.demo.infrastructure.api.Listener;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

class Subscriber {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final EventBus bus;
    private final Listener listener;
    private final Executor executor;

    Subscriber(EventBus bus, Listener listener) {
        this.bus = bus;
        this.listener = listener;
        this.executor = bus.getExecutor();
    }

    final void dispatchEvent(final String event) {
        synchronized (this) {
            executor.execute(() -> {

                try {
                    listener.onMessage(event);
                } catch (Exception e) {
                    logger.log(Level.SEVERE
                            , String.format("Cannot dispatch event {%s}.", event)
                            , e.getCause());
                }

            });
        }
    }

    Listener getListener() {
        return listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return bus.equals(that.bus) &&
                listener.equals(that.listener) &&
                executor.equals(that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bus, listener, executor);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Subscriber.class.getSimpleName() + "[", "]")
                .add("bus=" + bus)
                .add("listener=" + listener)
                .add("executor=" + executor)
                .toString();
    }
}
