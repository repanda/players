package com.example.demo;

import com.example.demo.infrastructure.Main;
import com.example.demo.infrastructure.api.Logger;
import com.example.demo.infrastructure.logger.SystemOutLogger;
import org.junit.jupiter.api.Test;

class FirstUnitTest {

    @Test
    public void init() {
        Logger logger = new SystemOutLogger();
        Main console = new Main(logger, true);

        console.runInit();
    }
}
