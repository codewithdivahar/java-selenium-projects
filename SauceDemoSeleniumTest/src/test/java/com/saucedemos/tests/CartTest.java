package com.saucedemos.tests;

import com.saucedemos.base.BaseTest;
import com.saucedemos.pages.CartPage;
import com.saucedemos.pages.LoginPage;
import com.saucedemos.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {
    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeMethod
    public void loginAndAddProduct() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.login("standard_user", "secret_sauce");

        // Add a product to cart before each test
        productsPage.addFirstProductToCart();
        cartPage = productsPage.goToCart();
    }

    @Test(priority = 1)
    public void testCartPageLoad() {
        // Verify cart page loads correctly
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
    }

    @Test(priority = 2)
    public void testCartHasItems() {
        // Verify cart contains the added item
        Assert.assertEquals(cartPage.getCartItemsCount(), 1, "Cart should contain 1 item");
    }

    @Test(priority = 3)
    public void testCartItemDetails() {
        // Verify cart item details
        String itemName = cartPage.getFirstItemName();
        String itemPrice = cartPage.getFirstItemPrice();

        Assert.assertFalse(itemName.isEmpty(), "Item name should not be empty");
        Assert.assertFalse(itemPrice.isEmpty(), "Item price should not be empty");
        Assert.assertTrue(itemPrice.startsWith("$"), "Item price should start with $");
    }

    @Test(priority = 4)
    public void testCheckoutButton() {
        // Verify checkout button is displayed and clickable
        Assert.assertTrue(cartPage.isCheckoutButtonDisplayed(), "Checkout button should be displayed");
    }

    @Test(priority = 5)
    public void testContinueShoppingButton() {
        // Verify continue shopping button works
        Assert.assertTrue(cartPage.isContinueShoppingButtonDisplayed(), "Continue shopping button should be displayed");

        ProductsPage returnedProductsPage = cartPage.continueShopping();
        Assert.assertTrue(returnedProductsPage.isProductsPageDisplayed(), "Should return to products page");
    }
}