package com.example.demo.infrastructure.api;

public interface Listener {

    void onMessage(String message);

    String getId();
}
