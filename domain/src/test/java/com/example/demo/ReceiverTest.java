package com.example.demo;

import com.example.demo.infrastructure.Main;
import com.example.demo.infrastructure.api.Logger;
import com.example.demo.infrastructure.logger.SystemOutLogger;
import org.junit.jupiter.api.Test;

class ReceiverTest {

    @Test
    public void receiver() {
        System.out.println("receiver test process id = " + ProcessHandle.current().pid());

        Logger logger = new SystemOutLogger();
        Main console = new Main(logger, true);

        console.runReceiver();
    }
}
