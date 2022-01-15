package com.example.demo;

class Player {

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public Message sendMessageTo(Player receiver, String message) {
        return new Message(message, 1);
    }

    public Message reply(Player initiator, Message message) {

        return new Message(message.getContent(), 1);
    }
}
