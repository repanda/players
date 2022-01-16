package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Conversation {

    private final long id;
    private List<Message> messages;

    public Conversation(long conversationId, List<Message> messages) {
        this.id = conversationId;
        this.messages = messages;
    }

    public Conversation() {
        id = UUID.randomUUID().getLeastSignificantBits();
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public long getId() {
        return id;
    }
}
