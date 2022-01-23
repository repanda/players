package com.example.demo;

import com.example.demo.domain.Conversation;
import com.example.demo.domain.Player;
import com.example.demo.domain.events.Message;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    void initiator_should_send_message_to_second_player() {

        Player initiator = new Player("initiator");
        Conversation conversation = new Conversation(initiator, new FakeLogger());

        Message message = initiator.startConversation("Hi", conversation);

        assertThat(message.getContent()).isEqualTo("Hi,1");
    }

//    @Test
//    void receiver_should_reply_to_initiator_player() {
//
//        Player initiator = new Player("initiator");
//        Player receiver = new Player("receiver");
//
//        Message message = initiator.sendMessageTo(receiver, "Hi", CONVERSATION_ID);
//
//        Message reply = receiver.reply(initiator, message, CONVERSATION_ID);
//
//        assertThat(reply.getContent()).isEqualTo("Hi,1,1");
//    }
}
