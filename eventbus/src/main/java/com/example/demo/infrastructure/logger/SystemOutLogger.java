package com.example.demo.infrastructure.logger;


/**
 * console logger
 */
public class SystemOutLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println(msg);
    }
}