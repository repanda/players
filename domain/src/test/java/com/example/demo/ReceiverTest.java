package com.example.demo;

import com.example.demo.infrastructure.Main;
import com.example.demo.infrastructure.logger.SystemOutLogger;
import org.junit.jupiter.api.Test;

class ReceiverTest {

    @Test
    public void runTest() {
        System.out.println("## receiver test process id = " + ProcessHandle.current().pid());

        Main console = new Main(new SystemOutLogger(), true);
        console.runReceiver();
    }
}
