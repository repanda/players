package com.example.demo;

import com.example.demo.event.Main;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ChatAcceptanceTest {


    @Test
    public void run() {
        ApplicationLogger logger = new ApplicationLogger();
        Main console = new Main(logger);
        console.run();

        Assertions.assertThat(logger.printAll())
                .isEqualToIgnoringWhitespace(
                        """ 
                                player: p1 send message: hi
                                player: p2 send message: hi,1
                                player: p1 send message: hi,1,1
                                player: p2 send message: hi,1,1,2
                                player: p1 send message: hi,1,1,2,2
                                player: p2 send message: hi,1,1,2,2,3
                                player: p1 send message: hi,1,1,2,2,3,3
                                player: p2 send message: hi,1,1,2,2,3,3,4
                                player: p1 send message: hi,1,1,2,2,3,3,4,4
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5,5,6
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9
                                player: p2 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10
                                player: p1 send message: hi,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10
                                                                """
                );
    }
}
