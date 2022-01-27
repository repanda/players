package com.example.demo.infrastructure;

import com.example.demo.domain.Player;
import com.example.demo.infrastructure.api.Logger;
import com.example.demo.infrastructure.eventbus.InMemoryEventBus;
import com.example.demo.infrastructure.logger.SystemOutLogger;

/**
 * Main class
 */
public class Main {


    private final Logger logger;
    private final boolean file;

    public Main(Logger logger, boolean file) {
        this.logger = logger;
        this.file = file;
    }

    public static void main(String[] args) {
        Main main = new Main(new SystemOutLogger(), true);
        main.run();

        System.exit(0);
    }

    public void run() {
        InMemoryEventBus bus = InMemoryEventBus.getInstance()
                .withLogger(logger);

        Player initiator = new Player("initiator", logger);
        Player receiver = new Player("player2", logger);

        bus.subscribe(initiator.getConversation());
        bus.subscribe(receiver.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
        bus.startBus();
    }

    public void runInit() {
        InMemoryEventBus bus = InMemoryEventBus.getInstance()
                .withFile(true);

        Player initiator = new Player("initiator", logger);

        bus.subscribe(initiator.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
        bus.startBus();

    }

    public void runReceiver() {
        InMemoryEventBus bus = InMemoryEventBus.getInstance()
                .withFile(true);

        Player receiver = new Player("player2", logger);

        bus.subscribe(receiver.getConversation());

        bus.startBus();
    }

}