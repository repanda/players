package com.example.demo.domain;


import com.example.demo.infrastructure.eventbus.Message;
import com.example.demo.infrastructure.api.Logger;

import java.util.Objects;

public class Player {

    private Conversation conversation;
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, Logger logger) {
        this.name = name;
        this.conversation = new Conversation(name);
    }

    public String getName() {
        return name;
    }

    public Message startConversation(String startMessage, Conversation conversation) {
        conversation.sendMessage(startMessage);
        return new Message(startMessage, startMessage, 1, conversation.id);//TODO delete
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