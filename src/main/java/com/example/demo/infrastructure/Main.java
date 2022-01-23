package com.example.demo.infrastructure;

import com.example.demo.domain.Conversation;
import com.example.demo.domain.Player;
import com.example.demo.domain.api.Logger;
import com.example.demo.infrastructure.eventbus.InMemoryEventBus;
import com.example.demo.infrastructure.logger.SystemOutLogger;

import java.util.concurrent.Executors;

/**
 * Main class
 */
public class Main {


    private static InMemoryEventBus bus;

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
        bus = new InMemoryEventBus("chat", Executors.newFixedThreadPool(1));

        Player initiator = new Player("initiator");
        Player receiver = new Player("player2");

        Conversation initiatorConversation = new Conversation(initiator, logger);
        Conversation receiverConversation = new Conversation(logger, receiver.getName());

        bus.subscribe(initiatorConversation);
        bus.subscribe(receiverConversation);

        initiator.startConversation("hi", initiatorConversation);


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