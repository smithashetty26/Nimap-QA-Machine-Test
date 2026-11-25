package com.nimap.qa.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://test.fieldforceconnect.com/auth/login");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {

        String username = System.getProperty("qa.username");

        if (username == null || username.isBlank()) {
            throw new IllegalStateException(
                "Set qa.username and qa.password in Run Configurations."
            );
        }

        String password = System.getProperty("qa.password");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                "Set qa.password in Run Configurations."
            );
        }

        return new Object[][] {
            { username, password, true },
            { username, "WrongPassword123", false }
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyLogin(String username,
                            String password,
                            boolean expectedSuccess) {

        // Enter username
        WebElement usernameField =
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input:not([type='password'])")
            ));

        usernameField.sendKeys(username);

        // Enter password
        driver.findElement(
            By.cssSelector("input[type='password']")
        ).sendKeys(password);

        // Click Sign In
        driver.findElement(
            By.xpath("//button[contains(normalize-space(.),'Sign In')]")
        ).click();

        if (expectedSuccess) {

            wait.until(ExpectedConditions.urlContains("dashboard"));

            Assert.assertTrue(
                driver.getCurrentUrl().contains("dashboard"),
                "Valid login should open Dashboard"
            );

        } else {

            wait.until(ExpectedConditions
                .textToBePresentInElementLocated(
                    By.tagName("body"), "Invalid"
                ));

            Assert.assertTrue(
                driver.findElement(By.tagName("body"))
                    .getText().contains("Invalid"),
                "Invalid login error should be displayed"
            );
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
