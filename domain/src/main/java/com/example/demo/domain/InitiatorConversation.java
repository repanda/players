package com.example.demo.domain;


import com.example.demo.api.EventBus;
import com.example.demo.api.Subscribe;


public class InitiatorConversation extends Conversation {

    private static final int MAX_RECEIVED_MESSAGES = 10;

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

    public void sendMessage(String content) {

        String newMessage = content + "," + sentCounter.incrementAndGet();
        bus.post(new Message(id, newMessage));
    }

}