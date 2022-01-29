package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * EventBus allows publish-subscribe communication between components.
 * It requires components to explicitly register to the bus using {@link #register(Object)} method.
 */
public class EventBus {

    private final String name;
    private final boolean isFile;
    private final Logger logger;
    private WatchService watchService;
    private final Subscribers subscribers = new Subscribers();

    /**
     * Creates a new EventBus instance.
     */
    public EventBus(String name, boolean isFile, Logger logger) {
        this.name = name;
        this.isFile = isFile;
        this.logger = logger;

        if (isFile) {
            watch();
        }
    }

    /**
     * Posts the given event to the event bus. The
     */
    public void post(Object event) {
        if (isFile) {
            publishToFile((Message) event);
        } else {
            dispatch(event);
        }
    }

    /**
     * Send the given event object to interested subscribers
     *
     * @param event event to send
     */
    private void dispatch(Object event) {
        Class<?> clazz = event.getClass();
        Map<Class<?>, Set<Invocation>> invocationsMap = subscribers.getInvocations();
        boolean containsKey = invocationsMap.containsKey(clazz);
        if (containsKey) {
            Message message = (Message) event;

            logger.log(String.format("player: %s send message: %s", message.sender(), message.payload()));

            Set<Invocation> invocations = invocationsMap.get(clazz);
            for (Invocation invocation : invocations) {
                invocation.invoke(event);
            }
        }
    }

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object the object whose subscriber methods should be registered.
     */
    public void register(Object object) {
        subscribers.register(object);
    }

    public void unregister(Object object) {
        subscribers.unregister(object);
    }


    public static final String DATA_FOLDER = "./";
    static final Path EVENTS_PATH = Paths.get(DATA_FOLDER + "chat");

    private void publishToFile(Message message) {
        try {
            String value = message.sender() + ":" + message.payload();
            Files.write(EVENTS_PATH, Collections.singleton(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void watch() {
        new Thread(() -> {
            try {
                monitor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void monitor() {
        System.out.println("## start file event watcher");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            this.watchService = FileSystems.getDefault().newWatchService();
            Files.createDirectories(Paths.get(DATA_FOLDER));

            Path path = Paths.get(DATA_FOLDER);
            path.register(watchService, ENTRY_MODIFY);

            boolean poll = true;
            while (poll) {
                poll = pollEvents(watchService);
            }
        } catch (IOException | InterruptedException | ClosedWatchServiceException e) {
            Thread.currentThread().interrupt();
        }

    }

    private boolean pollEvents(WatchService watchService) throws InterruptedException {
        WatchKey key = watchService.take();
        for (WatchEvent<?> event : key.pollEvents()) {
            notify(event);
        }
        return key.reset();
    }

    private void notify(WatchEvent<?> path) {
        try {
            String file = path.context().toString();
            if ("chat".equals(file)) {
                List<String> allLines = Files.readAllLines(EVENTS_PATH);
                for (String line : allLines) {
                    String sender = line.split(":")[0];
                    String payload = line.split(":")[1];

                    Message message = new Message(sender, payload);
                    dispatch(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
