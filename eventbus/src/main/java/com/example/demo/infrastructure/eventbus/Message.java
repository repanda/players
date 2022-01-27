package com.example.demo.infrastructure.eventbus;

import java.util.StringJoiner;

public class Message {

    String sender;
    String payload;
    private final int totalSent;
    private final String conversationId;

    public Message(String sender, String payload, int totalSent, String conversationId) {
        this.sender = sender;
        this.payload = payload;
        this.totalSent = totalSent;
        this.conversationId = conversationId;
    }

    public String getContent() {
        return "";
    }

    public String getSender() {
        return sender;
    }

    public String getPayload() {
        return payload;
    }

    public Long getId() {
        return 0L;
    }

    public int getTotalSent() {
        return totalSent;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                .add("sender=" + sender)
                .add("payload='" + payload + "'")
                .add("totalSent=" + totalSent)
                .toString();
    }

}
