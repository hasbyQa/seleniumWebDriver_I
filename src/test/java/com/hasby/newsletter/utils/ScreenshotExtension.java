package com.hasby.newsletter.utils;

import com.hasby.newsletter.base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotExtension implements TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotExtension.class);

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();
        String testName = context.getDisplayName();

        // Screenshot first, then quit
        if (test.getDriver() != null) {
            logger.info("Test FAILED — capturing screenshot: {}", testName);
            ScreenshotUtil.takeScreenshot(test.getDriver(), "FAILED_" + testName);
            test.getDriver().quit();
            logger.info("Browser closed after failure");
        }
    }
    @Override
    public void testSuccessful(ExtensionContext context) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();

        // No screenshot needed, just close browser
        if (test.getDriver() != null) {
            test.getDriver().quit();
            logger.info("Browser closed after success");
        }
    }
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();

        if (test.getDriver() != null) {
            test.getDriver().quit();
            logger.info("Browser closed after abort");
        }
    }
}
