package com.example.demo.domain;


import com.example.demo.api.EventBus;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * Player class
 */
public class Player {

    private final String name;
    private Conversation conversation;

    private Player(String name, Conversation conversation) {
        this.name = name;
        this.conversation = conversation;
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
        return hash(name);
    }


    /**
     * Builder to create a player with the necessary conversation type
     */
    public static class PlayerBuilder {

        private String name;
        private EventBus bus;
        private boolean isInitiator;

        public PlayerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PlayerBuilder bus(EventBus bus) {
            this.bus = bus;
            return this;
        }

        public PlayerBuilder initiator() {
            this.isInitiator = true;
            return this;
        }

        public Player createPlayer() {
            requireNonNull(name);
            requireNonNull(bus);

            Conversation conversation = isInitiator ? new InitiatorConversation(name, bus) : new Conversation(name, bus);
            return new Player(name, conversation);
        }
    }


}