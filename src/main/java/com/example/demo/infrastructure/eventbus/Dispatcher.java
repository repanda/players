package com.example.demo.infrastructure.eventbus;

import java.util.Iterator;

interface Dispatcher {

    void dispatch(String event, Iterator<Subscriber> subscribers);
}
