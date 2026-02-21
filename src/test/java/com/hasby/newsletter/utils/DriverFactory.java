package com.hasby.newsletter.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

//Factory class responsible for creating WebDriver instances
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
//    creates a chromedriver instance
    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Always run headless — consistent behavior locally and in CI
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--remote-allow-origins=*");

        logger.info("Running in HEADLESS mode");

        WebDriver driver = new ChromeDriver(options);
        logger.info("ChromeDriver created successfully");

        return driver;
    }
}
