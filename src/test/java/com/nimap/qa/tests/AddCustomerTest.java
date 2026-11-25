package com.nimap.qa.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.*;

public class AddCustomerTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String username = System.getProperty("qa.username");
        String password = System.getProperty("qa.password");

        if (username == null || password == null) {
            throw new IllegalStateException(
                "Set qa.username and qa.password in VM Arguments"
            );
        }

        driver.get("https://test.fieldforceconnect.com/auth/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("input:not([type='password'])")
        )).sendKeys(username);

        driver.findElement(
            By.cssSelector("input[type='password']")
        ).sendKeys(password);

        By loginButton = By.xpath(
            "//button[contains(normalize-space(.),'Sign In')]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Open My Customers
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[normalize-space()='My Customers']")
        )).click();

        wait.until(ExpectedConditions.urlContains("customers"));
    }

    @DataProvider(name = "customerData")
    public Object[][] customerData() {

        return new Object[][] {
            {"Smitha Automation Customer 01"},
            {"Smitha Automation Customer 02"}
        };
    }

    @Test(dataProvider = "customerData")
    public void addCustomer(String customerName) {

        // Click Manage
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(.,'Manage')]")
        )).click();

        // Click New Customer
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(normalize-space(.),'New Customer')]")
        )).click();

        // Enter required Customer Name
        WebElement nameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter Lead/Customer Name']")
            )
        );

        nameField.sendKeys(customerName);

        // Save customer
        By saveButton = By.xpath(
            "//button[normalize-space()='Save']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

        // Validate customer creation in the list
        boolean customerCreated = wait.until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"), customerName
            )
        );

        Assert.assertTrue(
            customerCreated,
            "Customer was not created: " + customerName
        );

        System.out.println("Customer created successfully: " + customerName);
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
