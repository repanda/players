package com.example.demo.domain;


import com.example.demo.api.EventBus;
import com.example.demo.api.Subscribe;

/**
 * Initiator Conversation of an initiator player, it allows a player to send and receive messages
 */
public class InitiatorConversation extends Conversation {

    private static final int MAX_RECEIVED_MESSAGES = 10;

    public InitiatorConversation(String name, EventBus bus) {
        super(name, bus);
    }

    /**
     * Consume the Message event dispatched by the bus.
     * Stop after this initiator sent 10 messages and received back 10 messages
     */
    @Subscribe
    public void onMessage(Message message) {
        if (this.id.equals(message.sender())) {
            return;
        }
        receivedCounter.incrementAndGet();

        //unregister after this initiator sent 10 messages and received back 10 messages
        int sentMessages = sentCounter.get();
        if (sentMessages >= MAX_SENT_MESSAGES && receivedCounter.get() >= MAX_RECEIVED_MESSAGES) {
            bus.unregister(this);
            return;
        }

        try { // just for demo purpose
            Thread.sleep(10L);
        } catch (InterruptedException ignored) {
        }

        sendMessage(message.payload());
    }

    /**
     * sends the message content to the bus
     */
    public void sendMessage(String content) {

        String newMessage = content + "," + sentCounter.incrementAndGet();
        bus.post(new Message(id, newMessage));
    }

}