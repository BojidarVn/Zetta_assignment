package com.bojidar.zetta.ui;

import com.bojidar.zetta.base.BaseTest;
import com.bojidar.zetta.config.Config;
import com.bojidar.zetta.models.Product;
import com.bojidar.zetta.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckoutFlowTest extends BaseTest {
    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";
    private static final String FIRST_NAME = "Teo";
    private static final String LAST_NAME = "Ivanov";
    private static final String POSTAL_CODE = "9000";


    @Test(description = "End-to-end checkout flow for the three cheapest products")
    public void standardUserShouldCheckoutThreeCheapestProducts() {
        LoginPage loginPage = new LoginPage(driver());

        loginPage.open();
        assertLoginPageIsDisplayed(loginPage);

        InventoryPage inventoryPage = loginPage.login(USERNAME, PASSWORD);
        assertInventoryPageIsDisplayed(inventoryPage);

        inventoryPage.sortByPriceLowToHigh();

        List<BigDecimal> actualPrices = inventoryPage.getDisplayedPrices();
        assertPricesAreSortedLowToHigh(actualPrices);

        List<Product> expectedProducts = inventoryPage.addThreeCheapestProductsToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 3,
                "Cart badge should show 3 added products.");

        CartPage cartPage = inventoryPage.openCart();
        assertCartPageContainsExpectedProducts(cartPage, expectedProducts);

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        assertCheckoutPageIsDisplayed(checkoutPage);

        checkoutPage.fillCheckoutInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        CheckoutOverviewPage overviewPage = checkoutPage.continueToOverview();

        assertCheckoutOverviewPageIsDisplayed(overviewPage);
        assertSubtotalMatchesProducts(overviewPage, expectedProducts);
    }

    private void assertLoginPageIsDisplayed(LoginPage loginPage) {
        Assert.assertEquals(loginPage.getCurrentUrl(), Config.sauceDemoUrl(),
                "Login page URL is incorrect.");

        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs",
                "Login page title is incorrect.");

        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                "Username field should be visible.");

        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                "Password field should be visible.");

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Login button should be visible.");
    }

    private void assertInventoryPageIsDisplayed(InventoryPage inventoryPage) {
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("/inventory.html"),
                "Inventory page URL should contain /inventory.html.");

        Assert.assertTrue(inventoryPage.isInventoryContainerDisplayed(),
                "Inventory container should be visible.");

        Assert.assertTrue(inventoryPage.isSortDropdownDisplayed(),
                "Sort dropdown should be visible.");

        Assert.assertTrue(inventoryPage.getProductsCount() > 0,
                "Inventory page should display products.");
    }

    private void assertPricesAreSortedLowToHigh(List<BigDecimal> actualPrices) {
        List<BigDecimal> expectedSortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedSortedPrices);

        Assert.assertEquals(actualPrices, expectedSortedPrices,
                "Product prices should be sorted from low to high.");
    }

    private void assertCartPageContainsExpectedProducts(CartPage cartPage, List<Product> expectedProducts) {
        Assert.assertTrue(cartPage.getCurrentUrl().contains("/cart.html"),
                "Cart page URL should contain /cart.html.");

        Assert.assertEquals(cartPage.getCartItemsCount(), expectedProducts.size(),
                "Cart should contain exactly the added products.");

        Assert.assertEquals(cartPage.getCartProducts(), expectedProducts,
                "Cart products and prices should match the products added from inventory.");

        Assert.assertTrue(cartPage.isCheckoutButtonDisplayed(),
                "Checkout button should be visible.");
    }

    private void assertCheckoutPageIsDisplayed(CheckoutPage checkoutPage) {
        Assert.assertTrue(checkoutPage.getCurrentUrl().contains("/checkout-step-one.html"),
                "Checkout page URL should contain checkout-step-one.html.");

        Assert.assertTrue(checkoutPage.isFirstNameFieldDisplayed(),
                "First name field should be visible.");

        Assert.assertTrue(checkoutPage.isLastNameFieldDisplayed(),
                "Last name field should be visible.");

        Assert.assertTrue(checkoutPage.isPostalCodeFieldDisplayed(),
                "Postal code field should be visible.");

        Assert.assertTrue(checkoutPage.isContinueButtonDisplayed(),
                "Continue button should be visible.");
    }

    private void assertCheckoutOverviewPageIsDisplayed(CheckoutOverviewPage overviewPage) {
        Assert.assertTrue(overviewPage.getCurrentUrl().contains("checkout-step-two"),
                "Checkout overview page URL is incorrect.");

        Assert.assertTrue(overviewPage.isSubtotalDisplayed(),
                "Subtotal should be displayed.");

        Assert.assertTrue(overviewPage.isFinishButtonDisplayed(),
                "Finish button should be visible on checkout overview page.");
    }

    private void assertSubtotalMatchesProducts(CheckoutOverviewPage overviewPage, List<Product> expectedProducts) {
        BigDecimal expectedSubtotal = expectedProducts.stream()
                .map(Product::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Assert.assertEquals(overviewPage.getDisplayedSubtotal(), expectedSubtotal,
                "Order subtotal should match the sum of the selected products.");
    }
}