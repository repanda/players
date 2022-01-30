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

    public Main(Logger logger) {
        this.logger = logger;
    }

    public static void main(String[] args) {
        Main main = createMainApplication(args);
        main.run();

    }

    private static Main createMainApplication(String[] args) {
        SystemOutLogger logger = new SystemOutLogger();
        if (args.length == 0) {
            return new Main(logger);
        }

        String playerName = args[0];
        if ("initiator".equalsIgnoreCase(playerName)) {
            return new MainInitiator(logger);
        } else {
            return new MainReceiver(logger, playerName);
        }
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

    void startBus(SimpleEventBus bus) {
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

    private static class MainInitiator extends Main {
        private final String playerName;

        public MainInitiator(Logger logger) {
            super(logger);
            this.playerName = "initiator";
        }

        public void run() {

            super.logger.log(String.format("Player %s has joined the chat", playerName));

            Dispatcher pollDispatcher = new PollDispatcher();
            SimpleEventBus bus = new SimpleEventBus(super.logger, pollDispatcher);

            Player initiator = new PlayerBuilder().name(playerName).bus(bus).initiator().createPlayer();

            bus.register(initiator.getConversation());

            initiator.startConversation("hi", initiator.getConversation());

            startBus(bus);
        }
    }

    private static class MainReceiver extends Main {
        private final String playerName;

        public MainReceiver(SystemOutLogger logger, String playerName) {
            super(logger);
            this.playerName = playerName;
        }

        public void run() {
            super.logger.log(String.format("Player %s has joined the chat", playerName));

            PollDispatcher pollDispatcher = new PollDispatcher();
            SimpleEventBus bus = new SimpleEventBus(super.logger, pollDispatcher);

            Player receiver = new PlayerBuilder()
                    .name(playerName).bus(bus).createPlayer();

            bus.register(receiver.getConversation());

            startBus(bus);
        }
    }
}