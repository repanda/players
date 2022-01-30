package com.example.demo.domain;


import com.example.demo.api.EventBus;
import com.example.demo.api.Subscribe;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class Conversation {

    protected static final int MAX_SENT_MESSAGES = 10;

    protected final AtomicInteger sentCounter = new AtomicInteger();
    protected final AtomicInteger receivedCounter = new AtomicInteger();

    protected final String id;
    protected final EventBus bus;

    public Conversation(String name, EventBus bus) {
        this.id = name;
        this.bus = bus;
    }

    /**
     * Consume the Message event dispatched by the bus.
     */
    @Subscribe("chat")
    public void onMessage(Message message) {
        if (this.id.equals(message.sender())) {
            return;
        }
        System.out.println(String.format("## player: %s receive message from %s: %s", this.id, message.sender(), message.payload()));
        receivedCounter.incrementAndGet();

        try { // juste for demo purpose
            Thread.sleep(1L);
        } catch (InterruptedException ignored) {
        }

        sendMessage(message.payload());
    }

    public void sendMessage(String content) {

        String newMessage = content + "," + sentCounter.incrementAndGet();
        bus.post(new Message(id, newMessage));

        if (sentCounter.get() >= MAX_SENT_MESSAGES) {
            bus.unregister(this);
        }
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