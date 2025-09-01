package com.saucedemos.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import java.time.Duration;

public class BaseTest {
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String BASE_URL = "https://www.saucedemo.com";

    public static WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless) {
        setupDriver(browser, Boolean.parseBoolean(headless));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        getDriver().manage().window().maximize();

        // Navigate to base URL
        getDriver().get(BASE_URL);

        // Wait for page to be ready
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                org.openqa.selenium.By.id("user-name")));
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    private void setupDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
            case "chrome-headless":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                // Essential Chrome options for automation
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
//                chromeOptions.addExperimentalOption("useAutomationExtension", false);
//                chromeOptions.addExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-plugins");
                chromeOptions.addArguments("--disable-images");
                chromeOptions.addArguments("--disable-javascript-harmony-shipping");
                chromeOptions.addArguments("--disable-default-apps");
                chromeOptions.addArguments("--no-default-browser-check");
                chromeOptions.addArguments("--no-first-run");
                chromeOptions.addArguments("--disable-default-apps");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-translate");
                chromeOptions.addArguments("--disable-background-timer-throttling");
                chromeOptions.addArguments("--disable-renderer-backgrounding");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");
                chromeOptions.addArguments("--disable-client-side-phishing-detection");
                chromeOptions.addArguments("--disable-sync");
                chromeOptions.addArguments("--metrics-recording-only");
                chromeOptions.addArguments("--no-report-upload");
                chromeOptions.addArguments("--disable-background-networking");

                if (headless || browser.contains("headless")) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }

                // Set user agent to make it look more like a real browser
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                try {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    if (headless) {
                        firefoxOptions.addArguments("--headless");
                    }

                    // Try to detect Firefox binary location on Mac
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        String[] possiblePaths = {
                                "/Applications/Firefox.app/Contents/MacOS/firefox",
                                "/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox",
                                "/usr/local/bin/firefox"
                        };

                        for (String path : possiblePaths) {
                            if (new java.io.File(path).exists()) {
                                firefoxOptions.setBinary(path);
                                break;
                            }
                        }
                    }
                    driver.set(new FirefoxDriver(firefoxOptions));
                } catch (Exception e) {
                    System.err.println("Firefox not found or failed to start. Falling back to Chrome...");
                    // Fallback to Chrome
                    WebDriverManager.chromedriver().setup();
                    chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    driver.set(new ChromeDriver(chromeOptions));
                }
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Execute script to remove webdriver property
        if (getDriver() instanceof ChromeDriver) {
            try {
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript(
                        "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            } catch (Exception e) {
                // Ignore if script fails
            }
        }
    }

    protected String getBaseUrl() {
        return BASE_URL;
    }
}