package com.example.demo;

import java.util.List;

class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void startConversation() {
        Player initiator = new Player("initiator");
        Player receiver = new Player("receiver");

        Message message = initiator.sendMessageTo(receiver, "Hi");

        // when a player receives a message
        Message response = initiator.reply(initiator, message);

    }

    public List<Message> loadMessages() {
        Message message1 = new Message("Hi", 1);
        return List.of(message1);
    }
}
