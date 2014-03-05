package uk.co.malbec.cascade;


import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.util.List;

public interface TestExecutor {

    void executeTest(RunNotifier notifier, Description description, List<Object> steps);
}