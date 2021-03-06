package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;

import static java.lang.Integer.parseInt;
import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@SuppressWarnings("all")
@Step(Login.class)
public interface Challenge {
    public class PassChallenge implements Challenge {

        @Supplies
        private String challengePhrase = "onceuponamidnightdreary";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            int one = parseInt(readText(webDriver, "[test-text-number-one]")) - 1;
            int two = parseInt(readText(webDriver, "[test-text-number-two]")) - 1;
            int three = parseInt(readText(webDriver, "[test-text-number-three]")) - 1;

            enterText(webDriver, "[test-field-number-one]", String.format("%c", challengePhrase.charAt(one)));
            enterText(webDriver, "[test-field-number-two]", String.format("%c", challengePhrase.charAt(two)));
            enterText(webDriver, "[test-field-number-three]", String.format("%c", challengePhrase.charAt(three)));

            click(webDriver, "[test-cta-authenticate]");

            waitForPage(webDriver);
        }
    }

    @Terminator
    public class FailChallenge implements Challenge {
        @Supplies
        private String challengePhrase = "onceuponamidnightdreary";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            enterText(webDriver, "[test-field-number-one]", "a");
            enterText(webDriver, "[test-field-number-two]", "a");
            enterText(webDriver, "[test-field-number-three]", "a");

            click(webDriver, "[test-cta-authenticate]");
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertElementPresent(webDriver,"[test-form-login]");
            assertElementDisplayed(webDriver, "[test-dialog-authentication-failure]");
        }
    }
}
