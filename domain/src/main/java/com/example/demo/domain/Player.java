package com.example.demo.domain;



import com.example.demo.api.EventBus;

import java.util.Objects;

public class Player {

    private Conversation conversation;
    private final String name;

    public Player(String name, EventBus bus) {
        this.name = name;
        this.conversation = new Conversation(name, bus);
    }

    public void startConversation(String startMessage, Conversation conversation) {
        conversation.sendMessage(startMessage);
    }

    public Conversation getConversation() {
        return conversation;
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