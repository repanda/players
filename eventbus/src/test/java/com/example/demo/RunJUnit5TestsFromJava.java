package com.example.demo;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnit5TestsFromJava {

    public void runOne(Class<?> clazz, TestExecutionListener listener) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(clazz))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
    }


    public static void main(String[] args) {
        runTest(ReceiverTest.class);
        runTest(InitiatorTest.class);
    }

    private static void runTest(Class<?> clazz) {
        new Thread(() -> {
            SummaryGeneratingListener listener = new SummaryGeneratingListener();

            RunJUnit5TestsFromJava runner = new RunJUnit5TestsFromJava();
            runner.runOne(clazz, listener);

            TestExecutionSummary summary = listener.getSummary();
            summary.printTo(new PrintWriter(System.out));
        }).start();
    }
}