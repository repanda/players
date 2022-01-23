package com.example.demo;

import com.example.demo.domain.api.Logger;

import java.util.ArrayList;
import java.util.List;

class FakeLogger implements Logger {

    private final List<String> logs = new ArrayList<>();

    public FakeLogger() {
    }

    @Override
    public void log(String msg) {
        logs.add(msg);
    }

    public String printAll() {
        return String.join("\n", logs);
    }

}
