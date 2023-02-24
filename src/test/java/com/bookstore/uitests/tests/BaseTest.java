package com.bookstore.uitests.tests;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BaseTest {

    WebDriver driver;
    private static final String CUSTOMERS_URL = "http://localhost:8080";

    public BaseTest() {
        driver = new FirefoxDriver();
        driver.get(CUSTOMERS_URL);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
