package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;

class ConsoleLogger {

    private final List<Message> messages;

    public ConsoleLogger(List<Message> messages) {
        this.messages = messages;
    }

    public String format() {
       return messages.stream()
                .map(message -> message.getContent())
                .collect(Collectors.joining());
    }
}
