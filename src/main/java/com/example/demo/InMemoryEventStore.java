package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryEventStore implements EventStore {

    private Map<Long, List<Message>> store = new HashMap<>();

    @Override
    public void store(Long messageId, List<Message> messages) {
        store.computeIfAbsent(messageId, id -> new ArrayList<>()).addAll(messages);
    }

    @Override
    public List<Message> load(Long messageId) {
        return store.get(messageId);
    }
}
