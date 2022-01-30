package com.example.demo.infrastructure.eventbus;

import com.example.demo.api.Subscribe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Subscriber methods associated with this EventBus instance
 */
class Subscribers {

    /**
     * Store all subscribers methods
     */
    private final Map<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = new ConcurrentHashMap<>();


    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object the object whose subscriber methods should be registered.
     */
    public void register(Object object) {

        Class<?> currentClass = object.getClass();
        List<Method> subscribeMethods = findSubscriptionMethods(currentClass);

        for (Method method : subscribeMethods) {
            // we know for sure that it has only one parameter
            Class<?> type = method.getParameterTypes()[0];
            if (subscribers.containsKey(type)) {
                Set<Subscriber> subscribers = Stream.concat(
                        this.subscribers.get(type).stream(),
                        Stream.of(new Subscriber(method, object))
                ).collect(Collectors.toSet());

                this.subscribers.get(type).add(new Subscriber(method, object));
                //this.subscribers.put(type, );
            } else {
                subscribers.put(type, new CopyOnWriteArraySet<Subscriber>(Set.of(new Subscriber(method, object))));
            }
        }
    }

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     */
    public void unregister(Object object) {
        Class<?> currentClass = object.getClass();
        while (currentClass != null) {
            List<Method> subscribeMethods = findSubscriptionMethods(currentClass);

            for (Method method : subscribeMethods) {
                Class<?> type = method.getParameterTypes()[0];
                if (subscribers.containsKey(type)) {
                    Set<Subscriber> invocationsSet = subscribers.get(type);
                    invocationsSet.remove(new Subscriber(method, object));

                    if (invocationsSet.isEmpty()) {
                        subscribers.remove(type);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        System.out.println("Subscribers.unregister");
    }

    /**
     * check if there is any @{@link Subscribe} annotated methods
     */
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
        return nonNull(method.getAnnotation(Subscribe.class));
    }

    /**
     * Gets a set of subscribers representing an immutable snapshot of all subscribers to the given event at
     * the time this method is called.
     */
    public Set<Subscriber> getSubscribers(Object event) {
        Class<?> clazz = requireNonNull(event.getClass());

        return subscribers.containsKey(event.getClass()) ?
                unmodifiableSet(subscribers.get(clazz)) :
                Set.of();
    }

    public boolean isEmpty() {
        return subscribers.isEmpty();
    }
}
