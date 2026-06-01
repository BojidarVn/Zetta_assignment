package com.bojidar.zetta.pages;

import com.bojidar.zetta.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.cssSelector("[data-test='username']");
    private final By passwordInput = By.cssSelector("[data-test='password']");
    private final By loginButton = By.cssSelector("[data-test='login-button']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(Config.sauceDemoUrl());
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public String getPageTitle() {

        return driver.getTitle();
    }

    public boolean isUsernameFieldDisplayed() {

        return isVisible(usernameInput);
    }

    public boolean isPasswordFieldDisplayed() {

        return isVisible(passwordInput);
    }

    public boolean isLoginButtonDisplayed() {

        return isVisible(loginButton);
    }

    public InventoryPage login(String username, String password) {

        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);

        return new InventoryPage(driver);
    }
}