package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.Listener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class InMemorySubscribers implements Subscribers {

    private final CopyOnWriteArraySet<Subscriber> subscribers;

    public InMemorySubscribers() {
        subscribers = new CopyOnWriteArraySet<>();
    }

    @Override
    public void removeSubscriberOf(Listener listener) {
        Set<Subscriber> subscribersToRemove = subscribers.stream()
                .filter(subscriber -> listener.equals(subscriber.getListener()))
                .collect(Collectors.toSet());

        subscribers.removeAll(subscribersToRemove);
    }

    @Override
    public void add(Listener listener, InMemoryEventBus bus) {
        subscribers.add(new Subscriber(bus, listener));
    }

    @Override
    public Set<Subscriber> getSubscribers(Predicate<String> excludes) {
        Predicate<String> predicate = (null == excludes) ? (s) -> true : excludes.negate();

        Set<Subscriber> subscribers = this.subscribers.stream()
                .filter(subscriber -> predicate.test(subscriber.getListener().getId()))
                .collect(Collectors.toSet());
        return subscribers;
    }

    @Override
    public boolean isEmpty() {
        return subscribers.isEmpty();
    }
}
