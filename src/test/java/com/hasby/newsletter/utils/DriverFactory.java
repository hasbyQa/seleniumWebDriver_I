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
    public static WebDriver createDriver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(System.getenv("HEADLESS"));
        if(headless){
            logger.info("Running in HEADLESS mod(CI)");
            options.addArguments("--headless=new"); //Modern headless mode
            options.addArguments("--no-sandbox");  //Required for Linux CI
            options.addArguments("--disable-dev-shm-usage");  //Prevent shared memory issues
            options.addArguments("--window-size=1920,1080");  //Consistent viewport in headless
        } else{
            logger.info("Running in HEADED mode (local development)");
            options.addArguments("--start-maximized"); //Full screen locally
        }

//        Suppress noisy Chrome/DevTools logs
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--remote-allow-origin=*");

        WebDriver driver = new ChromeDriver(options);
        logger.info("ChromeDriver created successfully");

        return driver;
    }
}
