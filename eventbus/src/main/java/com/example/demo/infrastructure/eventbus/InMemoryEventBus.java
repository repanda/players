package com.example.demo.infrastructure.eventbus;


import com.example.demo.infrastructure.api.*;
import com.example.demo.infrastructure.logger.SystemOutLogger;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class InMemoryEventBus implements EventBus, Serializable {

    private static final long serialVersionUID = 1L;
    public static boolean FILE = false;
    private String topic;
    private AtomicBoolean isEmpty = new AtomicBoolean(true);

    private final Dispatcher dispatcher;
    private final ExecutorService executor;
    private final Subscribers subscribers;

    private Logger logger;

    private InMemoryEventBus() {
        // private constructor
        this.executor = Executors.newFixedThreadPool(1);
        this.dispatcher = new BroadcastingDispatcher();
        this.logger = new SystemOutLogger();
        if (FILE) {// TODO dosnt work
            this.subscribers = new FileSubscribers(this);
            watch();
        } else {
            this.subscribers = new InMemorySubscribers();
        }
    }

    public InMemoryEventBus withLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public InMemoryEventBus withFile(boolean file) {
        FILE = file;
        return this;
    }

    private static class DemoSingletonHolder {
        public static final InMemoryEventBus INSTANCE = new InMemoryEventBus();
    }

    public static InMemoryEventBus getInstance() {
        return InMemoryEventBus.DemoSingletonHolder.INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }

    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void publish(Message message) {

        Publisher publisher = new Publisher() {
            @Override
            public String getPayload() {
                return message.payload;
            }

            @Override
            public Predicate<String> getFilters() {
                return (senderId) -> senderId.equals(message.sender);
            }
        };

        publish(message, publisher.getFilters());
    }

    @Override
    public void unsubscribe(Listener listener) {
        subscribers.removeSubscriberOf(listener);
        isEmpty.compareAndSet(false, subscribers.isEmpty());

        if (listener instanceof EventSubscriber) {
            ((EventSubscriber) listener).onUnsubscribe(this);
        }
    }

    public void startBus() {
        System.out.println("## InMemoryEventBus.startBus");
        while (true) {
            if (isEmpty.get()) {
                break;
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void subscribe(Listener listener) {
        subscribers.add(listener, this);
        System.out.println(String.format("Player {%s} has been joined to chat {%s}.", listener, this));
        isEmpty.set(false);
        if (listener instanceof EventSubscriber) {
            ((EventSubscriber) listener).onSubscribe(this);
        }
    }

    private void publish(Message message, Predicate<String> excludes) {
        if (FILE) {
            publishToFile(message);
        } else {
            dispatch(message, excludes);
        }
    }

    private void dispatch(Message message, Predicate<String> excludes) {
        Set<Subscriber> subscribers = this.subscribers.getSubscribers(excludes);
        dispatcher.dispatch(message.payload, subscribers.iterator());
        String value = String.format("player: %s send message: %s", message.sender, message.payload);
        System.out.println("value = " + value);
        logger.log(value);
    }

    public static final String DATA_FOLDER = "./data/";
    static final Path EVENTS_PATH = Paths.get(DATA_FOLDER + "chat");

    private void publishToFile(Message message) {
        try {
            System.out.println("dispatchFromFile message = " + message);
            String value = message.sender + ":" + message.payload;
            Files.write(EVENTS_PATH, Collections.singleton(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void watch() {
        executor.execute(() -> {
            System.out.println("start InMemoryEventBus.watch");
            WatchService watchService = null;
            try {
                watchService = FileSystems.getDefault().newWatchService();
                Files.createDirectories(Paths.get(DATA_FOLDER));

                Path path = Paths.get(DATA_FOLDER);
                path.register(watchService, ENTRY_MODIFY);
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean poll = true;
            while (poll) {
                WatchKey key = null;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind : " + event.kind() + " - File : " + event.context());
                    read(event);
                }
                poll = key.reset();
            }
        });

    }

    private void read(WatchEvent<?> path) {
        try {
            String file = path.context().toString();
            if ("chat".equals(file)) {
                List<String> allLines = Files.readAllLines(EVENTS_PATH);
                System.out.println("allLines.size() = " + allLines.size());
                for (String line : allLines) {
                    System.out.println(line);
                    String sender = line.split(":")[0];
                    String payload = line.split(":")[1];
                    Message message = new Message(sender, payload, 0, "");

                    dispatch(message, (senderId) -> senderId.equals(sender));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
