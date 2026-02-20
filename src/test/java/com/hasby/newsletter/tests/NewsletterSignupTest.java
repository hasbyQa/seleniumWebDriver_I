package com.hasby.newsletter.tests;

import com.hasby.newsletter.base.BaseTest;
import com.hasby.newsletter.pages.SignupPage;
import com.hasby.newsletter.pages.SuccessModalPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Newsletter Application")
@Feature("Signup Form")
@DisplayName("Newsletter Signup Tests")
public class NewsletterSignupTest extends BaseTest {
    //    Test data
    private static final String VALID_EMAIL = "test@example.com";

    private SignupPage signupPage;
    private SuccessModalPage modalPage;

    // Initialize page objects before each test
    @BeforeEach
    void initPages() {
        signupPage = new SignupPage(driver);
        modalPage = new SuccessModalPage(driver);
    }

    // POSITIVE TESTS (Happy Path)
    @Test
    @Severity(SeverityLevel.CRITICAL)
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
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("P2 - Verify the correct email is displayed in confirmation modal ")
    void testConfirmationEmailMatchesInput(){
        logger.info("TEST: Confirmation email text matches input");
        signupPage.submitEmail(VALID_EMAIL);
        modalPage.waitForModalVisible();

        String displayedEmail = modalPage.getConfirmationEmail();
        assertEquals(VALID_EMAIL, displayedEmail, "Confirmation email should match the entered email");
        logger.info("Confirmed email text: {}", displayedEmail);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("P3 - Verify you dismiss modal and then the form resets")
    void testDismissModalResetsForm(){
        logger.info("TEST: Dismiss modal resets form");

        // Subscribe first
        signupPage.submitEmail(VALID_EMAIL);
        modalPage.waitForModalVisible();
        logger.info("Modal appeared");

        // Dismiss
        modalPage.clickDismiss();
        modalPage.waitForModalHidden();
        logger.info("Modal dismissed");

        // Modal should be hidden
        assertFalse(modalPage.isDisplayed(), "Modal should be hidden after dismiss");

        // Email field should be cleared
        String emailValue = signupPage.getEmailFieldValue();
        assertEquals("", emailValue, "Email field should be empty after dismiss");
        logger.info("Form reset confirmed email field is empty");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("P4 -  Verify correct page title is displayed")
    void testPageTitle() {
        logger.info("TEST: Page title verification");

        String title = signupPage.getPageTitle();
        assertEquals("Newsletter Signup", title, "Page title should be 'Newsletter Signup'");
        logger.info("Page title verified: {}", title);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("P5 - Verify form accepts text input in email field")
    void testEmailFieldAcceptsInput() {
        logger.info("TEST: Email field accepts text input");

        // Verify the field is enabled and interactable
        assertTrue(signupPage.isEmailFieldEnabled(), "Email field should be enabled");

        // Type and verify the value is captured
        signupPage.enterEmail(VALID_EMAIL);
        String enteredValue = signupPage.getEmailFieldValue();
        assertEquals(VALID_EMAIL, enteredValue, "Email field should contain the typed text");
        logger.info("Email field accepted input: {}", enteredValue);
    }

//    NEGATIVE TESTS
    @Test
    @Severity(SeverityLevel.CRITICAL)
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

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("N2 - Verify error is shown for email missing @ symbol")
    void testEmailMissingAtSymbol() {
        logger.info("TEST: Email missing @ symbol");

        String invalidEmail = "testexample.com";
        signupPage.submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        signupPage.waitForError();

        assertTrue(signupPage.hasError(), "Error should be shown for email without @");
        logger.info("Error state verified for missing @ symbol");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("N3 - Verify error is shown for email missing domain")
    void testEmailMissingDomain() {
        logger.info("TEST: Email missing domain");

        String invalidEmail = "test@";
        signupPage.submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        signupPage.waitForError();

        assertTrue(signupPage.hasError(), "Error should be shown for email without domain");
        logger.info("Error state verified for missing domain");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("N4 - Verify error is shown for email missing TLD")
    void testEmailMissingTLD() {
        logger.info("TEST: Email missing top-level domain");

        String invalidEmail = "test@company";
        signupPage.submitEmail(invalidEmail);
        logger.info("Submitted invalid email: {}", invalidEmail);

        signupPage.waitForError();

        assertTrue(signupPage.hasError(), "Error should be shown for email without TLD (.com, .org, etc...)");
        logger.info("Error state verified for missing TLD");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("N5 - Verify error is cleared when user starts typing")
    void testErrorClearsOnInput(){
        logger.info("TEST: Error clears when user types");

        //Triggering the error first
        signupPage.submitEmail("");
        signupPage.waitForError();
        assertTrue(signupPage.hasError(), "Error should appear first");
        logger.info("Error triggered");

        signupPage.typeInEmailField("t");
        signupPage.waitForErrorToClear();

        assertFalse(signupPage.hasError(), "Error should clear when user starts typing");
        logger.info("Error cleared after user input");
    }
}
