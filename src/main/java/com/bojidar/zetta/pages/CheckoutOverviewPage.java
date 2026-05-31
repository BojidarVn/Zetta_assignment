package com.bojidar.zetta.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

public class CheckoutOverviewPage extends BasePage {

    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By finishButton = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public BigDecimal getDisplayedSubtotal() {
        String subtotalText = getText(subtotalLabel);
        String priceText = subtotalText.replaceAll("[^0-9.]", "");

        return new BigDecimal(priceText);
    }

    public boolean isSubtotalDisplayed() {

        return isVisible(subtotalLabel);
    }

    public boolean isFinishButtonDisplayed() {

        return isVisible(finishButton);
    }
}