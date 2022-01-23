package com.example.demo.domain.events;

import com.example.demo.domain.Player;

import java.util.StringJoiner;

public class Message {

    Player sender;
    Player receiver;
    String payload;
    private final int totalSent;
    private final long conversationId;

    public Message(String payload, int totalSent, long conversationId) {
        this.payload = payload;
        this.totalSent = totalSent;
        this.conversationId = conversationId;
    }

    public Message(Player sender, Player receiver, String payload, int totalSent, long conversationId) {
        this.sender = sender;
        this.receiver = receiver;
        this.payload = payload;
        this.totalSent = totalSent;
        this.conversationId = conversationId;
    }

    public String getContent() {
        return payload + "," + totalSent;
    }

    public Long getId() {
        return 0L;
    }

    public int getTotalSent() {
        return totalSent;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                .add("sender=" + sender)
                .add("payload='" + payload + "'")
                .add("totalSent=" + totalSent)
//                .add("conversationId=" + conversationId)
                .toString();
    }

}
