package com.example.demo.domain;


import com.example.demo.domain.events.Message;

import java.util.Objects;

public class Player {

    private final Conversation conversation;
    private final String name;

    public Player(String name) {
        this.name = name;
        conversation = null;
    }

    public Player(Conversation conversation, String name) {
        this.conversation = conversation;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Message startConversation(String startMessage, Conversation conversation) {
        conversation.sendMessage(startMessage);
        return new Message(startMessage,  1, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}