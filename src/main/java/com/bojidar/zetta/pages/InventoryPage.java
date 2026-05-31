package com.bojidar.zetta.pages;

import com.bojidar.zetta.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    private final By inventoryContainer = By.cssSelector("[data-test='inventory-container']");
    private final By productSortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By inventoryItems =  By.cssSelector("[data-test='inventory-item']");
    private final By productName = By.cssSelector("[data-test='inventory-item-name']");
    private final By productPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By addToCartButton = By.cssSelector("button[data-test^='add-to-cart']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By shoppingCartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public boolean isInventoryContainerDisplayed() {

        return isVisible(inventoryContainer);
    }

    public boolean isSortDropdownDisplayed() {

        return isVisible(productSortDropdown);
    }

    public int getProductsCount() {

        return driver.findElements(inventoryItems).size();
    }

    public void sortByPriceLowToHigh() {
        Select sortDropdown = new Select(waitForVisible(productSortDropdown));
        sortDropdown.selectByValue("lohi");
    }

    public List<BigDecimal> getDisplayedPrices() {

        return driver.findElements(productPrice)
                .stream()
                .map(WebElement::getText)
                .map(this::parsePrice)
                .toList();
    }

    public List<Product> addThreeCheapestProductsToCart() {
        List<WebElement> items = driver.findElements(inventoryItems);
        List<Product> addedProducts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            WebElement item = items.get(i);

            String name = item.findElement(productName).getText();
            BigDecimal price = parsePrice(item.findElement(productPrice).getText());

            item.findElement(addToCartButton).click();

            addedProducts.add(new Product(name, price));
        }

        return addedProducts;
    }

    public int getCartBadgeCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return 0;
        }

        return Integer.parseInt(getText(cartBadge));
    }

    public CartPage openCart() {
        click(shoppingCartLink);

        return new CartPage(driver);
    }

    private BigDecimal parsePrice(String priceText) {

        return new BigDecimal(priceText.replace("$", "").trim());
    }
}