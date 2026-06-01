package com.bojidar.zetta.pages;

import com.bojidar.zetta.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector("[data-test='inventory-item']");
    private final By productName = By.cssSelector("[data-test='inventory-item-name']");
    private final By productPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By checkoutButton = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public int getCartItemsCount() {

        return driver.findElements(cartItems).size();
    }

    public List<Product> getCartProducts() {
        return driver.findElements(cartItems)
                .stream()
                .map(item -> new Product(
                        item.findElement(productName).getText(),
                        parsePrice(item.findElement(productPrice).getText())
                ))
                .toList();
    }

    public boolean isCheckoutButtonDisplayed() {
        return isVisible(checkoutButton);
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);

        return new CheckoutPage(driver);
    }

    private BigDecimal parsePrice(String priceText) {

        return new BigDecimal(priceText.replace("$", "").trim());
    }
}