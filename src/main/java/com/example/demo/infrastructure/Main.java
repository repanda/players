package com.example.demo.infrastructure;

import com.example.demo.domain.Conversation;
import com.example.demo.domain.Player;
import com.example.demo.domain.api.EventBus;
import com.example.demo.domain.api.Logger;
import com.example.demo.infrastructure.eventbus.InMemoryEventBus;
import com.example.demo.infrastructure.logger.SystemOutLogger;

import java.util.concurrent.Executors;

/**
 * Main class
 */
public class Main {


    private final Logger logger;

    public Main(Logger logger) {
        this.logger = logger;
    }

    public static void main(String[] args) {
        Main main = new Main(new SystemOutLogger());
        main.run();

        System.exit(0);
    }

    public void run() {
        InMemoryEventBus bus = new InMemoryEventBus("chat", Executors.newFixedThreadPool(1));

        Player initiator = new Player("initiator", logger);
        Player receiver = new Player("player2", logger);

        bus.subscribe(initiator.getConversation());
        bus.subscribe(receiver.getConversation());

        initiator.startConversation("hi", initiator.getConversation());


        while (true) {
            if (bus.isEmpty()) {
                break;
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
        }
    }

}