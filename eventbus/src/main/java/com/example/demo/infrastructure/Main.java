package com.example.demo.infrastructure;


import com.example.demo.api.EventBus;
import com.example.demo.domain.Player;
import com.example.demo.infrastructure.eventbus.BroadcastDispatcher;
import com.example.demo.infrastructure.eventbus.Dispatcher;
import com.example.demo.infrastructure.eventbus.PollDispatcher;
import com.example.demo.infrastructure.eventbus.SimpleEventBus;
import com.example.demo.infrastructure.logger.Logger;
import com.example.demo.infrastructure.logger.SystemOutLogger;

import static com.example.demo.domain.Player.PlayerBuilder;

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
        Dispatcher broadcastDispatcher = new BroadcastDispatcher();
        EventBus bus = new SimpleEventBus(logger, broadcastDispatcher);

        Player initiator = new PlayerBuilder().name("initiator").bus(bus).initiator().createPlayer();
        Player receiver = new PlayerBuilder().name("khaled").bus(bus).createPlayer();

        bus.register(initiator.getConversation());
        bus.register(receiver.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
    }

    public void runInitiator() {
        Dispatcher pollDispatcher = new PollDispatcher();
        SimpleEventBus bus = new SimpleEventBus(logger, pollDispatcher);

        Player initiator = new PlayerBuilder().name("initiator").bus(bus).initiator().createPlayer();

        bus.register(initiator.getConversation());

        initiator.startConversation("hi", initiator.getConversation());

        startBus(bus);
    }

    public void runReceiver() {
        PollDispatcher pollDispatcher = new PollDispatcher();
        SimpleEventBus bus = new SimpleEventBus(logger, pollDispatcher);

        Player receiver = new PlayerBuilder()
                .name("khaled").bus(bus).createPlayer();

        bus.register(receiver.getConversation());

        startBus(bus);
    }

    private void startBus(SimpleEventBus bus) {
        while (true) {
            if (bus.isEmpty()) {
                System.out.println("finalize the program gracefully");
                System.exit(0);
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
        }
    }
}