package com.hasby.newsletter.pages;

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

    public void enterEmail(String email) {
        emailInput.sendKeys(email);
        logger.info("Entered email: {}", email);
    }
    public void clickSubscribe() {
        submitButton.click();
        logger.info("Clicked subscribe button");
    }
    public void submitEmail(String email) {
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        clickSubscribe();
    }
    public String getEmailFieldValue() {
        return emailInput.getAttribute("value");
    }
    public boolean isEmailFieldEnabled() {
        return emailInput.isEnabled();
    }
    public boolean hasError() {
        String classes = emailGroup.getAttribute("class");
        return classes != null && classes.contains("error");
    }
    public void waitForError() {
        wait.until(d -> hasError());
        logger.info("Error state detected");
    }
    public void waitForErrorToClear() {
        wait.until(d -> !hasError());
        logger.info("Error state cleared");
    }
    public void typeInEmailField(String text) {
        emailInput.sendKeys(text);
        logger.info("Typed '{}' in email field", text);
    }
    public String getPageTitle() {
        return driver.getTitle();
    }
}
