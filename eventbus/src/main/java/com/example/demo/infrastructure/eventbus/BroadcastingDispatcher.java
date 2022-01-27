package com.example.demo.infrastructure.eventbus;


import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

class BroadcastingDispatcher implements Dispatcher {

    private final ThreadLocal<Queue<Event>> eventsQueue = ThreadLocal.withInitial(ArrayDeque::new);

    private final ThreadLocal<Boolean> dispatching = ThreadLocal.withInitial(() -> false);

    @Override
    public void dispatch(String payload, Iterator<Subscriber> subscribers) {
        Queue<Event> queueForThread = eventsQueue.get();
        queueForThread.offer(new Event(payload, subscribers));

        if (!dispatching.get()) {
            dispatching.set(true);
            try {
                Event nextEvent;
                while ((nextEvent = queueForThread.poll()) != null) {
                    while (nextEvent.subscribers().hasNext()) {

                        Subscriber nextSubscriber = nextEvent.subscribers().next();
                        nextSubscriber.dispatchEvent(nextEvent.payload());
                    }
                }
            } finally {
                dispatching.remove();
                eventsQueue.remove();
            }
        }
    }
}
