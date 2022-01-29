package com.example.demo.infrastructure.eventbus;

import java.util.Set;

public interface Dispatcher {

    void dispatch(Object event, Set<Subscriber> subscribers);

}
