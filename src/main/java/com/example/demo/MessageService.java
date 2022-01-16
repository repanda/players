package com.example.demo;

import java.util.List;

class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Long startConversation() {
        Player initiator = new Player("initiator");
        Player receiver = new Player("receiver");

        Message message = initiator.sendMessageTo(receiver, "Hi");

        Message response = initiator.reply(initiator, message);

        messageRepository.store(response);
        return response.getId();
    }

    public List<Message> loadMessages(long messageId) {
        List<Message> messages = messageRepository.load(messageId);
        return messages;
    }
}
