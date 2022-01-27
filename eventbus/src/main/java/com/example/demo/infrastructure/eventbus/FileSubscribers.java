package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.EventBus;
import com.example.demo.infrastructure.api.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

class FileSubscribers implements Subscribers {


    public static final String DATA_FOLDER = "./data/";
    public static final String PATH = DATA_FOLDER + "subscribers";
    static final Path SUBSCRIBERS_FILE = Paths.get(PATH);
    private final EventBus bus;

    FileSubscribers(EventBus bus) {
        this.bus = bus;
        try {
            File file = new File(PATH);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Subscriber> getSubscribers(Predicate<String> excludes) {
        try {
            Files.lines(Path.of(PATH))
                    .map(sender -> {

                        Listener listener = new Listener() {
                            @Override
                            public void onMessage(String message) {

                            }

                            @Override
                            public String getId() {
                                return null;
                            }
                        };

                        return new Subscriber(bus, listener);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Set.of();
    }

    @Override
    public void add(Listener listener, InMemoryEventBus bus) {
        try {
            String listenerId = listener.getId() + ";";
            Files.write(SUBSCRIBERS_FILE, Collections.singleton(listenerId), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSubscriberOf(Listener listener) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
