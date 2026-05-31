package com.bojidar.zetta.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.cssSelector("[data-test='firstName']");
    private final By lastNameInput = By.cssSelector("[data-test='lastName']");
    private final By postalCodeInput = By.cssSelector("[data-test='postalCode']");
    private final By continueButton = By.cssSelector("[data-test='continue']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public boolean isFirstNameFieldDisplayed() {

        return isVisible(firstNameInput);
    }

    public boolean isLastNameFieldDisplayed() {

        return isVisible(lastNameInput);
    }

    public boolean isPostalCodeFieldDisplayed() {

        return isVisible(postalCodeInput);
    }

    public boolean isContinueButtonDisplayed() {

        return isVisible(continueButton);
    }

    public CheckoutPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);

        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        click(continueButton);

        return new CheckoutOverviewPage(driver);
    }
}