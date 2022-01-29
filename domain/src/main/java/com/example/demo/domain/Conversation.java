package com.example.demo.domain;

import com.example.demo.infrastructure.eventbus.EventBus;
import com.example.demo.infrastructure.eventbus.Message;
import com.example.demo.infrastructure.eventbus.Subscribe;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class Conversation {

    public static final int MAX_SENT_MESSAGES = 10;
    public static final int MAX_RECEIVED_MESSAGES = 10;

    private final AtomicInteger sentCounter = new AtomicInteger();
    private final AtomicInteger receivedCounter = new AtomicInteger();

    public final String id;
    private final EventBus bus;

    public Conversation(String name, EventBus bus) {
        this.id = name;
        this.bus = bus;
    }

    @Subscribe("chat")
    public void onMessage(Message message) {
        if (this.id.equals(message.sender())) {
            return;
        }
        receivedCounter.incrementAndGet();

        try { // juste for demo purpose
            Thread.sleep(100L);
        } catch (InterruptedException ignored) {
        }

        sendMessage(message.payload());
    }

    public void sendMessage(String content) {

        int sentMessages = sentCounter.get();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
//            bus.unregister(this);
            return;
        }
        String newMessage = content + "," + sentCounter.incrementAndGet();

        bus.post(new Message(id, newMessage));
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