package com.example.demo;

import java.util.List;

interface EventStore {

    void store(Long id, List<Message> payload);

    List<Message> load(Long id);
}
