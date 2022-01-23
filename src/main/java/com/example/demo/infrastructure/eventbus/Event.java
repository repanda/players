package com.example.demo.infrastructure.eventbus;

import java.util.Iterator;

record Event(String payload, Iterator<Subscriber> subscribers) {

}