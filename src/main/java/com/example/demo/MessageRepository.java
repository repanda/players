package com.example.demo;

import java.util.List;

class MessageRepository {

    private final EventStore eventStore;

    MessageRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void store(Conversation conversation) {
        eventStore.store(conversation.getId(), conversation.getMessages());
    }

    public List<Message> load(long conversationId) {
        Conversation conversation = new Conversation(conversationId, eventStore.load(conversationId));
        return conversation.getMessages();
    }
}
