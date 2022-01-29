package com.example.demo.infrastructure.eventbus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Subscribers {

    /**
     * Store all the subscribers associated with this EventBus instance
     */
    private Map<Class<?>, Set<Invocation>> invocations = new ConcurrentHashMap<>();

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
        return subscribes.length > 0;
    }


    public Map<Class<?>, Set<Invocation>> getInvocations() {
        return invocations;
    }

}
