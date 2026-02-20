package com.hasby.newsletter.base;

import com.hasby.newsletter.utils.DriverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;

public class BaseTest {

//    Protected so subclasses can access the driver directly
    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static final String BASE_URL = "https://hasby-shanessa.github.io/news_letter_webDev/";

//    Creates a fresh browser instance and navigates to the base URL
    @BeforeEach
    void setUp(){
        logger.info("**************** TEST SETUP STARTED ****************");
        driver = DriverFactory.createDriver();
        driver.get(BASE_URL);
        logger.info("Navigated to: {}", BASE_URL);
        logger.info("**************** TEST SETUP COMPLETED ****************");
    }

//    Closes the browser and releases resources
    void tearDown(){
        logger.info("**************** TEST TEARDOWN STARTED ****************");
        if(driver != null){
            driver.quit();
            logger.info("Browser closed successfully");
        }
        logger.info("**************** TEST TEARDOWN STARTED ****************");
    }
}
