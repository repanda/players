package com.example.demo.infrastructure.eventbus;

public record Message(String sender,
                      String payload) {
}
