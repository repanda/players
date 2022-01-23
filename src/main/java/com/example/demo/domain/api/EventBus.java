package com.example.demo.domain.api;

public interface EventBus {

    void publish(Publisher publisher);

    void unsubscribe(Listener listener);
}
