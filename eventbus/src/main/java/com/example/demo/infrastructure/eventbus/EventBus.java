package com.example.demo.infrastructure.eventbus;

import com.example.demo.infrastructure.api.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Simple implementation demonstrating generally how guava-like EventBus works, without all the
 * additional code of special cases handling and the usage of special guava collections
 */
public class EventBus {

    private Map<Class<?>, Set<Invocation>> invocations = new ConcurrentHashMap<>();

    private final String name;
    private final boolean isFile;
    private final Logger logger;
    private WatchService watchService;

    public EventBus(String name, boolean isFile, Logger logger) {
        this.name = name;
        this.isFile = isFile;
        this.logger = logger;

        if (isFile) {
            watch();
        }
    }

    public void post(Object object) {
        if (isFile) {
            publishToFile((Message) object);
        } else {
            dispatch(object);
        }
    }

    private void dispatch(Object object) {
        Class<?> clazz = object.getClass();
        boolean containsKey = invocations.containsKey(clazz);
        if (containsKey) {
            Message message = (Message) object;

            logger.log(String.format("player: %s send message: %s", message.getSender(), message.getPayload()));

            Set<Invocation> invocations = this.invocations.get(clazz);
            for (Invocation invocation : invocations) {
                invocation.invoke(object);
            }
        }
    }

    public void register(Object object) {
        /**
         * try to navigate the object tree back to {@link Object} class while
         * checking if there is any @{@link Subscribe} annotated methods
         */
        Class<?> currentClass = object.getClass();
        while (currentClass != null) {
            List<Method> subscribeMethods = findSubscriptionMethods(currentClass);

            for (Method method : subscribeMethods) {
                // we know for sure that it has only one parameter
                Class<?> type = method.getParameterTypes()[0];
                if (invocations.containsKey(type)) {
                    Set<Invocation> invocations = Stream.concat(
                            this.invocations.get(type).stream(),
                            Stream.of(new Invocation(method, object))
                    ).collect(Collectors.toSet());

                    this.invocations.put(type, invocations);
                } else {
                    invocations.put(type, Set.of(new Invocation(method, object)));
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    public void unregister(Object object) {
        Class<?> currentClass = object.getClass();
        while (currentClass != null) {
            List<Method> subscribeMethods = findSubscriptionMethods(currentClass);

            for (Method method : subscribeMethods) {
                Class<?> type = method.getParameterTypes()[0];
                if (invocations.containsKey(type)) {
                    Set<Invocation> invocationsSet = invocations.get(type);
                    invocationsSet.remove(new Invocation(method, object));

                    if (invocationsSet.isEmpty()) {
                        invocations.remove(type);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private List<Method> findSubscriptionMethods(Class<?> type) {
        List<Method> subscribeMethods = Arrays.stream(type.getDeclaredMethods())
                .filter(this::isSubscribed)
                .collect(Collectors.toList());
        return filterSingleParameterMethods(subscribeMethods);
    }

    private List<Method> filterSingleParameterMethods(List<Method> subscribeMethods) {
        return subscribeMethods.stream().filter(method -> method.getParameterCount() == 1)
                .collect(Collectors.toList());
    }

    private boolean isSubscribed(Method method) {
        Subscribe[] subscribes = method.getAnnotationsByType(Subscribe.class);
        return Arrays.stream(subscribes).anyMatch(subscribe -> this.name.equals(subscribe.value()));
    }

    public Map<Class<?>, Set<Invocation>> getInvocations() {
        return invocations;
    }

    public static final String DATA_FOLDER = "./";
    static final Path EVENTS_PATH = Paths.get(DATA_FOLDER + "chat");

    private void publishToFile(Message message) {
        try {
            String value = message.getSender() + ":" + message.getPayload();
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

                    Message message = new Message(sender, payload, 0, "");
                    dispatch(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
