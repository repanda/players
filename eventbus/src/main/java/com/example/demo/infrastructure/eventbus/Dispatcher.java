package com.example.demo.infrastructure.eventbus;

import java.util.Set;

/**
 * Dispatching event to subscribers, providing different event dispatching possibilities,
 * in memory using the {@link BroadcastDispatcher }
 * or inner process using {@link PollDispatcher }
 */
public interface Dispatcher {

    /**
     * dispatches event to subscribers
     */
    void dispatch(Object event, Set<Subscriber> subscribers);

}
