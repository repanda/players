package com.example.demo.domain;


import com.example.demo.infrastructure.api.EventBus;
import com.example.demo.infrastructure.api.EventSubscriber;
import com.example.demo.infrastructure.api.Listener;
import com.example.demo.infrastructure.eventbus.Message;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class Conversation implements Listener, EventSubscriber {

    public static final int MAX_SENT_MESSAGES = 10;
    public static final int MAX_RECEIVED_MESSAGES = 10;
    private final AtomicReference<EventBus> bus = new AtomicReference<>();

    private final AtomicInteger sentCounter = new AtomicInteger();
    private final AtomicInteger receivedCounter = new AtomicInteger();

    public final String id;

    public Conversation(String name) {
        this.id = name;
    }

    @Override
    public void onMessage(String message) {
        receivedCounter.incrementAndGet();

        String newMessage = message + "," + sentCounter.incrementAndGet();
        sendMessage(newMessage);
    }

    @Override
    public String getId() {
        return id;
    }

    public void sendMessage(String message) {
        EventBus eventBus = bus.get();
        if (eventBus == null) {
            return;
        }

        int sentMessages = sentCounter.get();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
            eventBus.unsubscribe(this);
        }

        eventBus.publish(new Message(id, message, sentMessages, id));
    }

    @Override
    public void onSubscribe(EventBus bus) {
        this.bus.set(bus);
    }

    @Override
    public void onUnsubscribe(EventBus bus) {
        this.bus.set(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation conversation = (Conversation) o;
        return id.equals(conversation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                '}';
    }
}