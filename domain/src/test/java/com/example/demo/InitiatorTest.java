package com.example.demo;

import com.example.demo.infrastructure.Main;
import com.example.demo.infrastructure.logger.SystemOutLogger;
import org.junit.jupiter.api.Test;

class InitiatorTest {

    @Test
    public void runTest() {
        System.out.println("## initiator test process id = " + ProcessHandle.current().pid());

        Main console = new Main(new SystemOutLogger(), true);
        console.runInitiator();
    }
}
