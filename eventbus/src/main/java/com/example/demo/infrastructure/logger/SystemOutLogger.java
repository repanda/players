package com.example.demo.infrastructure.logger;


import com.example.demo.infrastructure.api.Logger;

public class SystemOutLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println(msg);
    }
}