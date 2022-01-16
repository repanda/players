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

        Conversation conversation = new Conversation();

        conversation.addMessage(message);
        conversation.addMessage(response);

        messageRepository.store(conversation);
        return conversation.getId();
    }

    public List<Message> loadMessages(long messageId) {
        List<Message> messages = messageRepository.load(messageId);
        return messages;
    }
}
