package com.example.demo.infrastructure.eventbus;

import java.util.Set;

public class BroadcastDispatcher implements Dispatcher {

    /**
     * dispatches events to subscribers immediately in the same JAVA process.
     */
    @Override
    public void dispatch(Object event, Set<Subscriber> subscribers) {
        subscribers.forEach(subscriber -> subscriber.invoke(event));
    }
}
