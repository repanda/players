package com.example.demo.domain;


import com.example.demo.api.EventBus;
import com.example.demo.api.Subscribe;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class InitiatorConversation extends Conversation {

    public static final int MAX_SENT_MESSAGES = 10;
    public static final int MAX_RECEIVED_MESSAGES = 10;

    private final AtomicInteger sentCounter = new AtomicInteger();
    private final AtomicInteger receivedCounter = new AtomicInteger();


    public InitiatorConversation(String name, EventBus bus) {
        super(name, bus);
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

        int sentMessages = sentCounter.get();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
            bus.unregister(this);
            return;
        }

        try { // juste for demo purpose
            Thread.sleep(1L);
        } catch (InterruptedException ignored) {
        }

        sendMessage(message.payload());
    }

    public void sendMessage(String content) {

        int sentMessages = sentCounter.get();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
            bus.unregister(this);
            //  return;
        }
        String newMessage = content + "," + sentCounter.incrementAndGet();

        bus.post(new Message(id, newMessage));
    }
    
}