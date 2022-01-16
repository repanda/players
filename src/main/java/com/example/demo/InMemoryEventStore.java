package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryEventStore implements EventStore {

    private Map<Long, List<Message>> store = new HashMap<>();

    @Override
    public void store(Long messageId, Message message) {
        store.computeIfAbsent(messageId, id -> new ArrayList<>()).addAll(List.of(message));
    }

    @Override
    public List<Message> load(Long messageId) {
        return store.get(messageId);
    }
}
