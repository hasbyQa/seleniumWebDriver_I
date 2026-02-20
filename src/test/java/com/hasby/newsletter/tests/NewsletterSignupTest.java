package com.hasby.newsletter.tests;

import com.hasby.newsletter.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class NewsletterSignupTest extends BaseTest {
//    locators
    private static final By EMAIL_INPUT = By.id("email");
    private static final By SUBMIT_BTN = By.id("submit-btn");
    private static final By EMAIL_GROUP = By.id("email-group");
    private static final By MODAL = By.id("modal");
    private static final By CONFIRM_EMAIL = By.id("confirm-email");
    private static final By DISMISS_BTN = By.id("dismiss-btn");

//    Test data
    private static final String VALID_EMAIL = "test@example.com";

    private WebDriverWait getWait(){
        return new WebDriverWait(driver, Duration.ofSeconds(10)); //polls DOM until condition met or 10s timeout
    }

    private boolean hasErrorClass(){
        String classes = driver.findElement(EMAIL_GROUP).getAttribute("class");
        return classes != null && classes.contains("error");
    }

    //submits the forms with a given email
    private void submitEmail(String email){
        if(email != null && !email.isEmpty()){
            driver.findElement(EMAIL_INPUT).sendKeys(email);
        }
        driver.findElement(SUBMIT_BTN).click();
    }

    // POSITIVE TESTS (Happy Path)
    @Test
    @DisplayName("P1 - Verify you subscribe successfully with valid email")
    void testSuccessfulSubscription(){
        logger.info("TEST: Successful subscription with valid email");

        submitEmail(VALID_EMAIL);
        logger.info("Submitted email:{}", VALID_EMAIL);

        // Wait for success modal to become visible
        WebElement modal = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(MODAL)
        );
        assertTrue(modal.isDisplayed(), "Success modal should be visible");
        logger.info("Success modal appeared - subscription successful");
    }

    @Test
    @DisplayName("P2 - Verify the correct email is displayed in confirmation modal ")
    void testConfirmationEmailMatchesInput(){
        logger.info("TEST: Confirmation email text matches input");
        submitEmail(VALID_EMAIL);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(MODAL));

        String displayedEmail = driver.findElement(CONFIRM_EMAIL).getText();
        assertEquals(VALID_EMAIL, displayedEmail, "Confirmation email should match the entered email");
        logger.info("Confirmed email text: {}", displayedEmail);
    }

    @Test
    @DisplayName("P3 - Verify you dismiss modal and then the form resets")
    void testDismissModalResetsForm(){
        logger.info("TEST: Dismiss modal resets form");

        // Subscribe first
        submitEmail(VALID_EMAIL);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(MODAL));
        logger.info("Modal appeared");

        // Dismiss
        driver.findElement(DISMISS_BTN).click();
        getWait().until(ExpectedConditions.invisibilityOfElementLocated(MODAL));
        logger.info("Modal dismissed");

        // Modal should be hidden
        assertFalse(driver.findElement(MODAL).isDisplayed(), "Modal should be hidden after dismiss");

        // Email field should be cleared
        String emailValue = driver.findElement(EMAIL_INPUT).getAttribute("value");
        assertEquals("", emailValue, "Email field should be empty after dismiss");
        logger.info("Form reset confirmed email field is empty");
    }

    @Test
    @DisplayName("P4 -  Verify correct page title is displayed")
    void testPageTitle() {
        logger.info("TEST: Page title verification");

        String title = driver.getTitle();
        assertEquals("Newsletter Signup", title, "Page title should be 'Newsletter Signup'");
        logger.info("Page title verified: {}", title);
    }

    @Test
    @DisplayName("P5 - Verify form accepts text input in email field")
    void testEmailFieldAcceptsInput() {
        logger.info("TEST: Email field accepts text input");

        WebElement emailField = driver.findElement(EMAIL_INPUT);

        // Verify the field is enabled and interactable
        assertTrue(emailField.isEnabled(), "Email field should be enabled");

        // Type and verify the value is captured
        emailField.sendKeys(VALID_EMAIL);
        String enteredValue = emailField.getAttribute("value");
        assertEquals(VALID_EMAIL, enteredValue, "Email field should contain the typed text");
        logger.info("Email field accepted input: {}", enteredValue);
    }

//    NEGATIVE TESTS
    @Test
    @DisplayName("N1 - Verify error is shown when email is empty")
    void testEmptyEmailShowsError() {
        logger.info("TEST: Empty email validation");

        // Click subscribe without entering anything
        submitEmail("");
        logger.info("Submitted with empty email");

        // Wait for error class to appear
        getWait().until(d -> hasErrorClass());

        assertTrue(hasErrorClass(), "Error should be shown for empty email");
        logger.info("Error state verified for empty email");
    }

    @Test
    @DisplayName("N2 - Verify error is shown for email missing @ symbol")
    void testEmailMissingAtSymbol() {
        logger.info("TEST: Email missing @ symbol");

        String invalidEmail = "testexample.com";
        submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        getWait().until(d -> hasErrorClass());

        assertTrue(hasErrorClass(), "Error should be shown for email without @");
        logger.info("Error state verified for missing @ symbol");
    }

    @Test
    @DisplayName("N3 - Verify error is shown for email missing domain")
    void testEmailMissingDomain() {
        logger.info("TEST: Email missing domain");

        String invalidEmail = "test@";
        submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        getWait().until(d -> hasErrorClass());

        assertTrue(hasErrorClass(), "Error should be shown for email without domain");
        logger.info("Error state verified for missing domain");
    }

    @Test
    @DisplayName("N4 - Verify error is shown for email missing TLD")
    void testEmailMissingTLD() {
        logger.info("TEST: Email missing top-level domain");

        String invalidEmail = "test@company";
        submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        getWait().until(d -> hasErrorClass());

        assertTrue(hasErrorClass(), "Error should be shown for email without TLD (.com, .org, etc...)");
        logger.info("Error state verified for missing TLD");
    }

    @Test
    @DisplayName("N5 - Verify error is cleared when user starts typing")
    void testErrorClearsOnInput(){
        logger.info("TEST: Error clears when user types");

        //Triggering the error first
        submitEmail("");
        getWait().until(d -> hasErrorClass());
        assertTrue(hasErrorClass(), "Error should appear first");
        logger.info("Error triggered");
        driver.findElement(EMAIL_INPUT).sendKeys("t");
        getWait().until(d -> !hasErrorClass());

        assertFalse(hasErrorClass(), "Error should clear when user starts typing"); logger.info("Error cleared after user input");
    }
}
