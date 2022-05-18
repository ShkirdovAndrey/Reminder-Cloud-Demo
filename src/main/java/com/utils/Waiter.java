package com.utils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.utils.PropertyReader.getProperty;

public class Waiter {
    public static final int DEFAULT_TIMEOUT = 60;
    public static int DURATION_TIMEOUT = getProperty("wait_timeout") != null ? Integer.parseInt(getProperty("wait_timeout")) : DEFAULT_TIMEOUT;
    private static final Long DELAY_BETWEEN_CHECK = 1L;

    private static void waitFor(Supplier<Boolean> checkResultSupplier, String operation) {
        waitFor(checkResultSupplier, Duration.ofSeconds(DURATION_TIMEOUT), Duration.ofSeconds(DELAY_BETWEEN_CHECK), operation);
    }

    private static void waitFor(Supplier<Boolean> checkResultSupplier, Duration timeout, Duration delayBetweenChecks, String operation) {
        boolean checkResult;
        LocalDateTime endDateTime = LocalDateTime.now().plus(timeout);
        do {
            checkResult = checkResultSupplier.get();
            try {
                TimeUnit.MILLISECONDS.sleep(delayBetweenChecks.toMillis());
            } catch (Exception e) {
                //do nothing
            }
        } while (endDateTime.isAfter(LocalDateTime.now()) && !checkResult);

        String message = System.lineSeparator() + "Operation is: " + operation;
        Assert.assertTrue(checkResult, "Waiting for 'check result' to be 'true' is failed due to timeout - " + timeout.toMinutes() + " min." + message);
    }

    public static void waitForElementAppears(SelenideElement element) {
        String message = "Waiting for Element: " + element + "\nTimeout: " + DURATION_TIMEOUT + " sec.";
        waitFor(() -> element.is(Condition.visible), message);
    }

    public static void waitForElementAppears(ElementsCollection elements) {
        String message = "Waiting for Elements Collection: " + elements + "\nTimeout: " + DURATION_TIMEOUT + " sec.";
        waitFor(() -> elements.size() > 0, message);
    }
}
