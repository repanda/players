package com.example.demo.domain;

/**
 * Object describing a message event,
 * and it's associated metadata, it's this what's going to
 * get sent in the bus to be dispatched to interested Subscribers
 *
 * @param sender  the message sender identifier
 * @param payload the message content
 */
public record Message(
        String sender,
        String payload
) {
}
