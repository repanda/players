package com.example.demo;

import com.example.demo.infrastructure.Main;
import com.example.demo.infrastructure.api.Logger;
import com.example.demo.infrastructure.logger.SystemOutLogger;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatAcceptanceTest {

    @Test
    public void should_send_messages_between_two_players() {
        FakeLogger logger = new FakeLogger();
        Main console = new Main(logger, false);

        console.run();

        assertThat(logger.printAll())
                .isEqualToIgnoringWhitespace(
                        """ 
                                player: initiator send message: hi,1
                                player: khaled send message: hi,1,1
                                player: initiator send message: hi,1,1,2
                                player: khaled send message: hi,1,1,2,2
                                player: initiator send message: hi,1,1,2,2,3
                                player: khaled send message: hi,1,1,2,2,3,3
                                player: initiator send message: hi,1,1,2,2,3,3,4
                                player: khaled send message: hi,1,1,2,2,3,3,4,4
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5,5,6
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5,6,6
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9
                                player: initiator send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10
                                player: khaled send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10
                                                                                                """
                );
    }

    @Test
    public void init() {
        Logger logger = new SystemOutLogger();
        Main console = new Main(logger, true);

        console.runInit();
    }

    @Test
    public void receiver() {
        Logger logger = new SystemOutLogger();
        Main console = new Main(logger, true);

        console.runReceiver();
    }
}
