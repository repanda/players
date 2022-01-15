package com.example.demo;

class Player {

    public Player(String initiator) {
    }

    public Message sendMessageTo(Player receiver, String message) {

        return new Message("Hi,1");
    }

    public Message reply() {
        return new Message("");
    }
}
