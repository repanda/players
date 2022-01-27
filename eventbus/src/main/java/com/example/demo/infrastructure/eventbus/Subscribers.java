package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.Listener;

import java.util.Set;
import java.util.function.Predicate;

public interface Subscribers {
    Set<Subscriber> getSubscribers(Predicate<String> excludes);

    void add(Listener listener, InMemoryEventBus bus);

    void removeSubscriberOf(Listener listener);

    boolean isEmpty();
}
