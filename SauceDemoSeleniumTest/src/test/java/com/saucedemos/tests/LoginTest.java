package com.saucedemos.tests;

import com.saucedemos.base.BaseTest;
import com.saucedemos.pages.LoginPage;
import com.saucedemos.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");

        // Perform login with valid credentials
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");

        // Verify successful login
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after login");
    }

    @Test(priority = 2)
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Attempt login with invalid credentials
        loginPage.login("invalid_user", "invalid_password");

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message should contain login failure text");
    }

    @Test(priority = 3)
    public void testEmptyUsernameLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Attempt login with empty username
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error message should indicate username is required");
    }

    @Test(priority = 4)
    public void testEmptyPasswordLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Attempt login with empty password
        loginPage.enterUsername("standard_user");
        loginPage.clickLogin();

        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Error message should indicate password is required");
    }

    @Test(priority = 5)
    public void testLockedUserLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Attempt login with locked user
        loginPage.login("locked_out_user", "secret_sauce");

        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error message should indicate user is locked out");
    }
}