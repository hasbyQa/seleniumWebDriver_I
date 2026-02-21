package com.hasby.newsletter.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupPage {
    private static final Logger logger = LoggerFactory.getLogger(SignupPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "submit-btn")
    private WebElement submitButton;

    @FindBy(id = "email-group")
    private WebElement emailGroup;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("SignupPage initialized");
    }

    //Actions (what a user can DO on this page)

    @Step("Enter email: {email}")
    public void enterEmail(String email) {
        emailInput.sendKeys(email);
        logger.info("Entered email: {}", email);
    }
    @Step("Click subscribe button")
    public void clickSubscribe() {
        submitButton.click();
        logger.info("Clicked subscribe button");
    }
    @Step("Submit email: {email}")
    public void submitEmail(String email) {
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        clickSubscribe();
    }
    @Step("Get email field value")
    public String getEmailFieldValue() {
        return emailInput.getAttribute("value");
    }
    @Step("Check if email field is enabled")
    public boolean isEmailFieldEnabled() {
        return emailInput.isEnabled();
    }
    @Step("Check for error state")
    public boolean hasError() {
        String classes = emailGroup.getAttribute("class");
        return classes != null && classes.contains("error");
    }
    @Step("Wait for error to appear")
    public void waitForError() {
        wait.until(d -> hasError());
        logger.info("Error state detected");
    }
    @Step("Wait for error to clear")
    public void waitForErrorToClear() {
        wait.until(d -> !hasError());
        logger.info("Error state cleared");
    }
    @Step("Type '{text}' in email field")
    public void typeInEmailField(String text) {
        emailInput.sendKeys(text);
        logger.info("Typed '{}' in email field", text);
    }
    @Step("Get page title")
    public String getPageTitle() {
        return driver.getTitle();
    }
    @Step("Get error message aria-live attribute")
    public String getErrorMessageAriaLive() {
        WebElement errorMsg = driver.findElement(By.cssSelector(".error-message"));
        return errorMsg.getAttribute("aria-live");
    }

    @Step("Get error message role attribute")
    public String getErrorMessageRole() {
        WebElement errorMsg = driver.findElement(By.cssSelector(".error-message"));
        return errorMsg.getAttribute("role");
    }
}
