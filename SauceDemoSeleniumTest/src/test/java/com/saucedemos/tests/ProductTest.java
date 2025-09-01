package com.saucedemos.tests;

import com.saucedemos.base.BaseTest;
import com.saucedemos.pages.LoginPage;
import com.saucedemos.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {
    private ProductsPage productsPage;

    @BeforeMethod
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.login("standard_user", "secret_sauce");

        // Wait for products page to fully load
        productsPage.waitForPageToLoad();
    }

    @Test(priority = 1)
    public void testProductsPageLoad() {
        // Verify products page loads correctly
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed");
        Assert.assertTrue(productsPage.getProductCount() > 0, "Products should be available");
    }

    @Test(priority = 2)
    public void testProductCount() {
        // Verify expected number of products
        int productCount = productsPage.getProductCount();
        Assert.assertEquals(productCount, 6, "Should display 6 products");
    }

    @Test(priority = 3)
    public void testAddSingleProductToCart() {
        // Get product name before adding to cart
        String productName = productsPage.getProductName(0);
        Assert.assertFalse(productName.isEmpty(), "Product name should not be empty");

        // Verify cart is initially empty
        Assert.assertFalse(productsPage.isCartBadgeVisible(), "Cart badge should not be visible initially");

        // Add first product to cart
        productsPage.addFirstProductToCart();

        // Verify cart badge appears and shows 1 item
        Assert.assertTrue(productsPage.isCartBadgeVisible(), "Cart badge should be visible after adding product");
        Assert.assertEquals(productsPage.getCartItemCount(), 1,
                "Cart should contain 1 item after adding product");
        Assert.assertEquals(productsPage.getCartBadgeText(), "1",
                "Cart badge should show '1' after adding product");
    }

    @Test(priority = 4)
    public void testAddSpecificProductToCart() {
        // Verify cart is initially empty
        Assert.assertEquals(productsPage.getCartItemCount(), 0, "Cart should be empty initially");

        // Add specific product by name
        productsPage.addProductToCart("Sauce Labs Backpack");

        // Verify cart badge shows 1 item
        Assert.assertTrue(productsPage.isCartBadgeVisible(), "Cart badge should be visible");
        Assert.assertEquals(productsPage.getCartItemCount(), 1,
                "Cart should contain 1 item after adding Sauce Labs Backpack");
    }

    @Test(priority = 5)
    public void testProductInformation() {
        // Verify product information is displayed
        String firstProductName = productsPage.getProductName(0);
        String firstProductPrice = productsPage.getProductPrice(0);

        Assert.assertFalse(firstProductName.isEmpty(), "Product name should not be empty");
        Assert.assertFalse(firstProductPrice.isEmpty(), "Product price should not be empty");
        Assert.assertTrue(firstProductPrice.startsWith("$"), "Product price should start with $");

        // Verify we have expected product names
        Assert.assertEquals(firstProductName, "Sauce Labs Backpack",
                "First product should be Sauce Labs Backpack");
    }

    @Test(priority = 6)
    public void testAddMultipleProducts() {
        // Add first product
        productsPage.addFirstProductToCart();
        Assert.assertEquals(productsPage.getCartItemCount(), 1, "Cart should have 1 item");

        // Add second product (Sauce Labs Bike Light)
        productsPage.addProductToCart("Sauce Labs Bike Light");
        Assert.assertEquals(productsPage.getCartItemCount(), 2, "Cart should have 2 items");
    }

    @Test(priority = 7)
    public void testLogout() {
        // Perform logout
        productsPage.logout();

        // Verify we're back to login page
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should return to login page after logout");
    }
}