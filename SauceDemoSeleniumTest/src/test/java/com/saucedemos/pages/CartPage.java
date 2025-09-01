package com.saucedemos.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CartPage {
    private WebDriver driver;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "cart_quantity")
    private List<WebElement> itemQuantities;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCartPageDisplayed() {
        return pageTitle.isDisplayed() && pageTitle.getText().equals("Your Cart");
    }

    public int getCartItemsCount() {
        return cartItems.size();
    }

    public String getFirstItemName() {
        if (!itemNames.isEmpty()) {
            return itemNames.get(0).getText();
        }
        return "";
    }

    public String getFirstItemPrice() {
        if (!itemPrices.isEmpty()) {
            return itemPrices.get(0).getText();
        }
        return "";
    }

    public void clickCheckout() {
        checkoutButton.click();
    }

    public ProductsPage continueShopping() {
        continueShoppingButton.click();
        return new ProductsPage(driver);
    }

    public boolean isCheckoutButtonDisplayed() {
        return checkoutButton.isDisplayed();
    }

    public boolean isContinueShoppingButtonDisplayed() {
        return continueShoppingButton.isDisplayed();
    }
}