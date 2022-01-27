package com.example.demo.infrastructure.api;


public interface EventSubscriber {

    void onSubscribe(EventBus bus);

    void onUnsubscribe(EventBus bus);
}
