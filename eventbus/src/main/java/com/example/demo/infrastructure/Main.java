package com.example.demo.infrastructure;


import com.example.demo.api.EventBus;
import com.example.demo.domain.Player;
import com.example.demo.infrastructure.eventbus.BroadcastDispatcher;
import com.example.demo.infrastructure.eventbus.Dispatcher;
import com.example.demo.infrastructure.eventbus.PollDispatcher;
import com.example.demo.infrastructure.eventbus.SimpleEventBus;
import com.example.demo.infrastructure.logger.Logger;
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
        Dispatcher broadcastDispatcher = new BroadcastDispatcher();
        EventBus bus = new SimpleEventBus(logger, broadcastDispatcher);

        Player initiator = new Player("initiator", bus);
        Player receiver = new Player("khaled", bus);

        bus.register(initiator.getConversation());
        bus.register(receiver.getConversation());

        initiator.startConversation("hi", initiator.getConversation());
    }

    public void runInitiator() {
        Dispatcher pollDispatcher = new PollDispatcher();
        SimpleEventBus bus = new SimpleEventBus(logger, pollDispatcher);

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
        PollDispatcher pollDispatcher = new PollDispatcher();
        SimpleEventBus bus = new SimpleEventBus(logger, pollDispatcher);

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