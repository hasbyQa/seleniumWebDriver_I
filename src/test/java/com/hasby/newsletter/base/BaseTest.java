package com.hasby.newsletter.base;

import com.hasby.newsletter.pages.SignupPage;
import com.hasby.newsletter.pages.SuccessModalPage;
import com.hasby.newsletter.utils.DriverFactory;
import com.hasby.newsletter.utils.ScreenshotExtension;
import com.hasby.newsletter.utils.ScreenshotUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.TestInfo;

@ExtendWith(ScreenshotExtension.class)
public class BaseTest {

//    Protected so subclasses can access the driver directly
    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected SignupPage signupPage;
    protected SuccessModalPage modalPage;
    protected static final String BASE_URL = "https://hasby-shanessa.github.io/news_letter_webDev/";

    // Tracks if current test failed (for screenshot capture)
    private boolean testFailed = false;

//    Creates a fresh browser instance and navigates to the base URL
    @BeforeEach
    void setUp(){
        logger.info("**************** TEST SETUP STARTED ****************");
        driver = DriverFactory.createDriver();
        driver.get(BASE_URL);
        logger.info("Navigated to: {}", BASE_URL);
        //Page objects initialization
        signupPage = new SignupPage(driver);
        modalPage = new SuccessModalPage(driver);
        logger.info("Page objects initialized");
        logger.info("**************** TEST SETUP COMPLETED ****************");
    }

//    Closes the browser and releases resources
    @AfterEach
    void tearDown() {
        logger.info("*************** TEST TEARDOWN ***************");
    }
    // Used by ScreenshotExtension to access the driver
    public WebDriver getDriver() {
        return driver;
    }
}
