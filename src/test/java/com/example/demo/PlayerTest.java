package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    /**
     * Acceptance test
     */
    @Test
    void receiver_should_reply_with_a_message_that_contains_the_received_message_concatenated_with_the_value_of_a_counter_holding_the_number_of_messages_this_player_already_sent() {

        Player initiator = new Player("initiator");
        Player receiver = new Player("receiver");

        initiator.sendMessageTo(receiver, "Hi");

        // when a player receives a message
        Message response = receiver.reply();

        //it should reply with a message that contains the received message
        // concatenated with the value of a counter holding the number of messages this player already sent.
        assertThat(response.getContent()).isEqualTo("Hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10");
    }

    @Test
    void initiator_should_send_message_to_second_player() {

        Player initiator = new Player("initiator");
        Player receiver = new Player("receiver");

        Message message = initiator.sendMessageTo(receiver, "Hi");

        assertThat(message.getContent()).isEqualTo("Hi,1");
    }
}
