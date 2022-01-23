package com.example.demo.domain;


import com.example.demo.domain.api.*;
import com.example.demo.domain.events.Message;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;


public class Conversation implements Listener, EventSubscriber {

    public static final int MAX_SENT_MESSAGES = 10;
    public static final int MAX_RECEIVED_MESSAGES = 10;
    private final AtomicReference<EventBus> bus = new AtomicReference<>();

    private final AtomicInteger sentCounter = new AtomicInteger();
    private final AtomicInteger receivedCounter = new AtomicInteger();

    private final Logger logger;

    public final String id;
    private List<Message> messages;

    public Conversation(Logger logger, String name) {
        this.logger = logger;
        this.id = name;
    }

    public Conversation(Logger logger) {
        this.logger = logger;
        id = String.valueOf(UUID.randomUUID().getLeastSignificantBits());
    }

    public Conversation(Player sender, Logger logger) {
        this.id = sender.getName();
        this.logger = logger;
    }

    @Override
    public void onMessage(String message) {
        receivedCounter.incrementAndGet();
        sendMessage(message);
    }

    public void sendMessage(String message) {

        EventBus eventBus = bus.get();
        if (eventBus == null) {
            return;
        }

        int sentMessages = sentCounter.incrementAndGet();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
            eventBus.unsubscribe(this);
        }

        Publisher publisher = new Publisher() {
            @Override
            public String getPayload() {
                String newMessage = message + "," + sentMessages;
                String value = String.format("player: %s send message: %s", id, message);
                logger.log(value);
                return newMessage;
            }

            @Override
            public Predicate<Listener> getFilters() {
                return predicate;
            }
        };

        eventBus.publish(publisher);

    }

    private final Predicate<Listener> predicate = (listener) -> listener.equals(Conversation.this);

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

}