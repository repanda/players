package com.example.demo.infrastructure;

import com.example.demo.domain.Player;
import com.example.demo.infrastructure.api.Logger;
import com.example.demo.infrastructure.eventbus.BroadcastDispatcher;
import com.example.demo.infrastructure.eventbus.EventBus;
import com.example.demo.infrastructure.eventbus.PollDispatcher;
import com.example.demo.infrastructure.logger.SystemOutLogger;

/**
 * Main class
 */
public class Main {


    private final Logger logger;
    private final boolean isFile;

    public Main(Logger logger, boolean isFile) {
        this.logger = logger;
        this.isFile = isFile;
    }

    public static void main(String[] args) {
        Main main = new Main(new SystemOutLogger(), false);
        main.run();

        System.exit(0);
    }

    public void run() {
        EventBus bus = new EventBus(logger, new BroadcastDispatcher());

        Player initiator = new Player("initiator", bus);
        Player receiver = new Player("khaled", bus);

        bus.register(initiator.getConversation());
        bus.register(receiver.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
    }

    public void runInitiator() {
        EventBus bus = new EventBus(logger, new PollDispatcher());

        Player initiator = new Player("initiator", bus);

        bus.register(initiator.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
        while (true) {

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException ignored) {
            }
        }

    }

    public void runReceiver() {
        EventBus bus = new EventBus(logger, new PollDispatcher());

        Player receiver = new Player("khaled", bus);

        bus.register(receiver.getConversation());
        while (true) {

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException ignored) {
            }
        }
    }

}