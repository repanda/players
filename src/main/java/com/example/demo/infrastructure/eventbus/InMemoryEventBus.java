package com.example.demo.infrastructure.eventbus;

import com.example.demo.domain.api.EventBus;
import com.example.demo.domain.api.EventSubscriber;
import com.example.demo.domain.api.Listener;
import com.example.demo.domain.api.Publisher;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryEventBus implements EventBus {

    private final String topic;
    private final AtomicBoolean isEmpty = new AtomicBoolean(true);

    private final Dispatcher dispatcher;
    private final ExecutorService executor;
    private final CopyOnWriteArraySet<Subscriber> subscribers = new CopyOnWriteArraySet<>();

    public InMemoryEventBus(String topic, ExecutorService executor) {
        this.dispatcher = new BroadcastingDispatcher();
        this.executor = executor;
        this.topic = topic;
    }

    Executor getExecutor() {
        return executor;
    }

    @Override
    public void publish(Publisher publisher) {
        publish(publisher.getPayload(), publisher.getFilters());
    }

    public void subscribe(Listener listener) {

        subscribers.add(new Subscriber(this, listener));

        isEmpty.set(false);

        if (listener instanceof EventSubscriber) {
            ((EventSubscriber) listener).onSubscribe(this);
        }
    }


    @Override
    public void unsubscribe(Listener listener) {

        Set<Subscriber> subscribersToRemove = subscribers.stream()
                .filter(subscriber -> listener.equals(subscriber.getListener()))
                .collect(Collectors.toSet());

        subscribers.removeAll(subscribersToRemove);

        isEmpty.compareAndSet(false, subscribers.isEmpty());

        if (listener instanceof EventSubscriber) {
            ((EventSubscriber) listener).onUnsubscribe(this);
        }
    }

    private void publish(String message, Predicate<Listener> excludes) {
        Predicate<Listener> predicate = (null == excludes) ? (s) -> true : excludes.negate();

        Set<Subscriber> subscribers = this.subscribers.stream()
                .filter(subscriber -> predicate.test(subscriber.getListener()))
                .collect(Collectors.toSet());

        dispatcher.dispatch(message, subscribers.iterator());
    }

    public boolean isEmpty() {
        return isEmpty.get();
    }

}
