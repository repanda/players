package com.example.demo.infrastructure.eventbus;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Polls events stored in an event log file {@code CHAT_FILE} and notify all subscribers.
 * This is used in inner process communication
 */

public class PollDispatcher implements Runnable, Dispatcher {

    private final String EVENT_FOLDER = "./";
    private final String CHAT_FILE = "chat";
    private final Path EVENTS_PATH = Paths.get(EVENT_FOLDER + CHAT_FILE);

    private final File folder = new File(EVENT_FOLDER);

    private Subscribers subscribers = new Subscribers();

    public PollDispatcher() {
        watch();
    }

    /**
     * inject Subscribers to be used in notification method {@link #notifySubscribers(WatchEvent)}
     */
    public void setSubscribers(Subscribers subscribers) {
        this.subscribers = subscribers;
    }

    public void watch() {
        if (folder.exists()) {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * watches modifications in the event log file
     */
    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(folder.getAbsolutePath());
            path.register(watchService, ENTRY_MODIFY);
            boolean poll = true;
            while (poll) {
                poll = pollEvents(watchService);
            }
        } catch (IOException | InterruptedException | ClosedWatchServiceException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Polls events occurred to the event log file
     */
    private boolean pollEvents(WatchService watchService) throws InterruptedException {
        WatchKey key = watchService.take();

        Optional<WatchEvent<?>> chatEvent = key.pollEvents().stream()
                .filter(path -> CHAT_FILE.equals(path.context().toString()))
                .findFirst();

        chatEvent.ifPresent(this::notifySubscribers);

        return key.reset();
    }

    /**
     * read the event log file and notify subscribers
     */
    private void notifySubscribers(WatchEvent<?> path) {
        try {
            List<String> allLines = Files.readAllLines(EVENTS_PATH);
            for (String line : allLines) {
                String sender = line.split(":")[0];
                String payload = line.split(":")[1];

                Message event = new Message(sender, payload);
                subscribers.getSubscribers(event)
                        .forEach(subscriber -> subscriber.invoke(event));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Store event to log file to be intercepted by the event watcher
     */
    @Override
    public void dispatch(Object event, Set<Subscriber> subscribers) {
        try {
            String value = ((Message) event).sender() + ":" + ((Message) event).payload();
            Files.write(EVENTS_PATH, Collections.singleton(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}