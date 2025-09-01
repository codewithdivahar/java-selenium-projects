package com.saucedemos.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> products;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isProductsPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfAllElements(products));
        return products.size();
    }

    public void addProductToCart(String productName) {
        // Convert product name to id attribute value
        String replace = productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "");
        String addButtonId = "add-to-cart-" + replace;
        String removeButtonId = "remove-" + replace;

        // Find and click the add button using id
        By addButtonLocator = By.id(addButtonId);
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));

        // Scroll to button
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addButton);

        // Wait for scroll
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Use JavaScript click (regular click doesn't work with SauceDemo)
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", addButton);

        // Wait for the button to change to remove button (confirms item was added)
        By removeButtonLocator = By.id(removeButtonId);
        wait.until(ExpectedConditions.presenceOfElementLocated(removeButtonLocator));
        wait.until(ExpectedConditions.textToBe(removeButtonLocator, "Remove"));

        // Wait for cart badge to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge")));
    }

    public void addFirstProductToCart() {
        // Wait for page to be fully loaded first
        waitForPageToLoad();

        // Use the specific id for the first product (Sauce Labs Backpack)
        By addButtonLocator = By.id("add-to-cart-sauce-labs-backpack");

        // Wait for the button to be present, visible, and clickable
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));

        // Scroll to the button to ensure it's in view
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addButton);

        // Wait for scroll to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Use JavaScript click (regular click doesn't work with SauceDemo)
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", addButton);

        // Wait for the remove button to appear
        By removeButtonLocator = By.id("remove-sauce-labs-backpack");
        wait.until(ExpectedConditions.presenceOfElementLocated(removeButtonLocator));
        wait.until(ExpectedConditions.textToBe(removeButtonLocator, "Remove"));

        // Wait for cart badge to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge")));
        wait.until(ExpectedConditions.textToBe(By.className("shopping_cart_badge"), "1"));
    }

    public String getCartBadgeText() {
        try {
            WebElement badge = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge"))
            );
            return badge.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    public int getCartItemCount() {
        try {
            WebElement badge = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge"))
            );
            String badgeText = badge.getText().trim();
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCartBadgeVisible() {
        try {
            driver.findElement(By.className("shopping_cart_badge"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        return new CartPage(driver);
    }

    public String getProductName(int index) {
        if (index < products.size()) {
            return products.get(index).findElement(By.className("inventory_item_name")).getText();
        }
        return "";
    }

    public String getProductPrice(int index) {
        if (index < products.size()) {
            return products.get(index).findElement(By.className("inventory_item_price")).getText();
        }
        return "";
    }

    public void logout() {
        try {
            // Step 1: Click hamburger menu using JavaScript (regular clicks don't work reliably)
            WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("react-burger-menu-btn")));

            // Scroll to menu button if needed
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", menu);

            // JavaScript click on menu button
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", menu);

            // Wait for sidebar menu to appear
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("bm-menu")));

            // Additional wait for animations to complete
            Thread.sleep(1000);

            // Step 2: Click logout link using JavaScript
            WebElement logout = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("logout_sidebar_link")));

            // Wait for logout link to be visible (sidebar might still be animating)
            wait.until(ExpectedConditions.visibilityOf(logout));

            // JavaScript click on logout link
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", logout);

            // Wait to return to login page (URL should change)
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("inventory")));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during logout process", e);
        } catch (Exception e) {
            throw new RuntimeException("Logout failed: " + e.getMessage(), e);
        }
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        wait.until(ExpectedConditions.visibilityOfAllElements(products));
    }

    public void clearCart() {
        // Remove all items from cart by clicking Remove buttons
        try {
            var removeButtons = driver.findElements(org.openqa.selenium.By.cssSelector("[id^='remove-']"));
            for (org.openqa.selenium.WebElement removeButton : removeButtons) {
                try {
                    if (removeButton.isDisplayed() && removeButton.getText().equals("Remove")) {
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                                "arguments[0].click();", removeButton);
                        Thread.sleep(500); // Brief wait between clicks
                    }
                } catch (Exception e) {
                    // Continue to next button if this one fails
                }
            }
        } catch (Exception e) {
            // Cart might already be empty
        }
    }

    public boolean isProductInCart(String productName) {
        String replace = productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "");
        String removeButtonId = "remove-" + replace;

        try {
            return driver.findElements(org.openqa.selenium.By.id(removeButtonId)).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}