package com.bojidar.zetta.pages;

import com.bojidar.zetta.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.defaultTimeoutSeconds()));
    }

    protected WebElement waitForVisible(By locator) {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {

        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {

        return waitForVisible(locator).getText();
    }

    protected boolean isVisible(By locator) {

        return !driver.findElements(locator).isEmpty()
                && driver.findElement(locator).isDisplayed();
    }

    protected void waitForUrlContains(String value) {
        wait.until(ExpectedConditions.urlContains(value));
    }

    protected <T> T waitUntil(ExpectedCondition<T> condition) {

        return wait.until(condition);
    }
}