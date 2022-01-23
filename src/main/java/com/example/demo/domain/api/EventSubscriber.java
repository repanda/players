package com.example.demo.domain.api;


public interface EventSubscriber {

    void onSubscribe(EventBus bus);

    void onUnsubscribe(EventBus bus);
}
