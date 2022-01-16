package com.example.demo;

import java.util.List;

class MessageRepository {

    private final EventStore eventStore;

    MessageRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void store(Message message) {
        eventStore.store(message.getId(), message);
    }

    public List<Message> load(long messageId) {
       return eventStore.load(messageId);
    }
}
