package com.example.demo;

class Message {

    String payload;
    private final int totalSent;

    public Message(String payload, int totalSent) {
        this.payload = payload;
        this.totalSent = totalSent;
    }

    public String getContent() {
        return payload + "," + totalSent;
    }
}
