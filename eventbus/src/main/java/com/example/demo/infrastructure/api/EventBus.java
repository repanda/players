package com.example.demo.infrastructure.api;

import com.example.demo.infrastructure.eventbus.Message;

import java.util.concurrent.Executor;

public interface EventBus {

    void publish(Message message);

    void unsubscribe(Listener listener);

    Executor getExecutor();
}
