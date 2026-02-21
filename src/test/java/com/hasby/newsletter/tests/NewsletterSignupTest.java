package com.hasby.newsletter.tests;

import com.hasby.newsletter.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Newsletter Application")
@Feature("Signup Form")
@Owner("Hasbiyallah")
@Link(name = "Newsletter Page", url = "https://hasby-shanessa.github.io/news_letter_webDev/")
@DisplayName("Newsletter Signup Tests")
public class NewsletterSignupTest extends BaseTest {
    //    Test data
    private static final String VALID_EMAIL = "test@example.com";

    // POSITIVE TESTS (Happy Path)
    @Test
    @Story("Successful Subscription")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user can subscribe with a valid email and the success modal appears")
    @DisplayName("P1 - Verify you subscribe successfully with valid email")
    void testSuccessfulSubscription(){
        logger.info("TEST: Successful subscription with valid email");

        signupPage.submitEmail(VALID_EMAIL);
        logger.info("Submitted email:{}", VALID_EMAIL);
        // Wait for success modal to become visible
        modalPage.waitForModalVisible();
        assertTrue(modalPage.isDisplayed(), "Success modal should be visible");
        logger.info("Success modal appeared - subscription successful");
    }

    @Test
    @Story("Successful Subscription")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the confirmation modal displays the exact email that was entered")
    @DisplayName("P2 - Verify the correct email is displayed in confirmation modal")
    void testConfirmationEmailMatchesInput(){
        logger.info("TEST: Confirmation email text matches input");
        signupPage.submitEmail(VALID_EMAIL);
        modalPage.waitForModalVisible();

        String displayedEmail = modalPage.getConfirmationEmail();
        assertEquals(VALID_EMAIL, displayedEmail, "Confirmation email should match the entered email");
        logger.info("Confirmed email text: {}", displayedEmail);
    }

    @Test
    @Story("Modal Interaction")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that dismissing the modal hides it and resets the email field")
    @DisplayName("P3 - Verify you dismiss modal and then the form resets")
    void testDismissModalResetsForm(){
        logger.info("TEST: Dismiss modal resets form");

        signupPage.submitEmail(VALID_EMAIL);
        modalPage.waitForModalVisible();
        logger.info("Modal appeared");

        modalPage.clickDismiss();
        modalPage.waitForModalHidden();
        logger.info("Modal dismissed");

        assertFalse(modalPage.isDisplayed(), "Modal should be hidden after dismiss");

        String emailValue = signupPage.getEmailFieldValue();
        assertEquals("", emailValue, "Email field should be empty after dismiss");
        logger.info("Form reset confirmed email field is empty");
    }

    @Test
    @Story("Page Load")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the page title matches the expected value")
    @DisplayName("P4 - Verify correct page title is displayed")
    void testPageTitle() {
        logger.info("TEST: Page title verification");

        String title = signupPage.getPageTitle();
        assertEquals("Newsletter Signup", title, "Page title should be 'Newsletter Signup'");
        logger.info("Page title verified: {}", title);
    }

    @Test
    @Story("Form Interaction")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the email input field is enabled and accepts text input")
    @DisplayName("P5 - Verify form accepts text input in email field")
    void testEmailFieldAcceptsInput() {
        logger.info("TEST: Email field accepts text input");

        assertTrue(signupPage.isEmailFieldEnabled(), "Email field should be enabled");

        signupPage.enterEmail(VALID_EMAIL);
        String enteredValue = signupPage.getEmailFieldValue();
        assertEquals(VALID_EMAIL, enteredValue, "Email field should contain the typed text");
        logger.info("Email field accepted input: {}", enteredValue);
    }

//    NEGATIVE TESTS
    @Test
    @Story("Email Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify submitting with empty email shows validation error")
    @DisplayName("N1 - Verify error is shown when email is empty")
    void testEmptyEmailShowsError() {
        logger.info("TEST: Empty email validation");
    // Click subscribe without entering anything
        signupPage.submitEmail("");
        logger.info("Submitted with empty email");

    // Wait for error class to appear
        signupPage.waitForError();

        assertTrue(signupPage.hasError(), "Error should be shown for empty email");
        logger.info("Error state verified for empty email");
    }

//    Parameterized tests
    @ParameterizedTest(name = "N2-N4 - Invalid email: {0} ({1})")
    @CsvSource({
            "testexample.com, missing @ symbol",
            "test@, missing domain",
            "test@company, missing TLD"
    })
    @Story("Email Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that various invalid email formats trigger validation error")
    void testInvalidEmailShowsError(String invalidEmail, String reason) {
        logger.info("TEST: Invalid email - {}", reason);

        signupPage.submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {} ({})", invalidEmail, reason);

        signupPage.waitForError();

        assertTrue(signupPage.hasError(),
                "Error should be shown for email with " + reason);
        logger.info("Error state verified for {}", reason);
    }

    @Test
    @Story("Email Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the error state clears when user starts typing a correction")
    @DisplayName("N5 - Verify error is cleared when user starts typing")
    void testErrorClearsOnInput(){
        logger.info("TEST: Error clears when user types");

        signupPage.submitEmail("");
        signupPage.waitForError();
        assertTrue(signupPage.hasError(), "Error should appear first");
        logger.info("Error triggered");

        signupPage.typeInEmailField("t");
        signupPage.waitForErrorToClear();

        assertFalse(signupPage.hasError(), "Error should clear when user starts typing");
        logger.info("Error cleared after user input");
    }

    //Failing Tests
    @Test
    @Story("Input Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("BUG REPORT: Email field has no maximum character limit, allowing excessively long input")
    @DisplayName("F1 - BUG: Email field should enforce maximum character limit")
    void testEmailFieldHasMaxLength() {
        logger.info("TEST: Email field maximum character limit");

        // Generate a very long email string (over 100 characters)
        String longEmail = "a".repeat(300) + "@example.com";
        signupPage.enterEmail(longEmail);
        String enteredValue = signupPage.getEmailFieldValue();
        assertTrue(enteredValue.length() <= 254,
                "BUG-001 | Email field accepts " + enteredValue.length() + " characters. " + "Expected: maxlength of 254 (RFC 5321 standard). " +
                        "Actual: No maxlength attribute set on input#email. " + "Steps: Navigate to signup → enter 312 chars in email field → field accepts all with no limit. "
                        + "Severity: Minor | Priority: Low | Status: Open | " + "Fix: Add maxlength=\"254\" to <input id=\"email\">");
        logger.info("Email field character limit verified");
    }

    @Test
    @Story("Accessibility")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG REPORT: Error state lacks ARIA attributes for screen reader accessibility")
    @DisplayName("F2 - BUG: Error message should have aria-live attribute for accessibility")
    void testErrorMessageHasAriaAttributes() {
        logger.info("TEST: Error message ARIA accessibility");

        // Trigger the error state
        signupPage.submitEmail("");
        signupPage.waitForError();
        logger.info("Error state triggered");
        // Check for aria-live or role="alert" on the error message
        String ariaLive = signupPage.getErrorMessageAriaLive();
        String role = signupPage.getErrorMessageRole();

        assertTrue(ariaLive != null || "alert".equals(role),
                "BUG-002 | Error message lacks accessibility attributes. " + "Expected: aria-live=\"polite\" or role=\"alert\" on .error-message element. " + "Actual: aria-live="
                        + ariaLive + ", role=" + role + ". " + "Steps: Navigate to signup → leave email empty → click Subscribe → " + "inspect .error-message span for ARIA attributes → none found. "
                        + "Severity: Normal (WCAG 2.1 violation) | Priority: Medium | Status: Open | " + "Fix: Add role=\"alert\" aria-live=\"polite\" to <span class=\"error-message\">");
        logger.info("ARIA attributes verified on error message");
    }
}
