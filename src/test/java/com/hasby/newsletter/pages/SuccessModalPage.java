package com.hasby.newsletter.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SuccessModalPage {
    private static final Logger logger = LoggerFactory.getLogger(SuccessModalPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    //Locators
    @FindBy(id = "modal")
    private WebElement modal;

    @FindBy(id = "confirm-email")
    private WebElement confirmEmail;

    @FindBy(id = "dismiss-btn")
    private WebElement dismissButton;

    public SuccessModalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("SuccessModalPage initialized");
    }

    //Actions
    @Step("Wait for success modal to appear")
    public void waitForModalVisible() {
        wait.until(ExpectedConditions.visibilityOf(modal));
        logger.info("Success modal is visible");
    }
    @Step("Wait for success modal to disappear")
    public void waitForModalHidden() {
        wait.until(ExpectedConditions.invisibilityOf(modal));
        logger.info("Success modal is hidden");
    }
    @Step("Check if modal is displayed")
    public boolean isDisplayed() {
        return modal.isDisplayed();
    }
    @Step("Check if modal is displayed")
    public String getConfirmationEmail() {
        return confirmEmail.getText();
    }
    @Step("Click dismiss button")
    public void clickDismiss() {
        dismissButton.click();
        logger.info("Clicked dismiss button");
    }
}
