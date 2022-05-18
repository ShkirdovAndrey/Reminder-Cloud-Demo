package com.ui.utils;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.utils.PropertyReader.getProperty;

public class BrowserFactory {

    public static void setBrowser(String browser, String environment, String selenoidUrl, String testName, String mobile) throws Exception {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));

        baseUrl = "https://" + getProperty("baseUrl");
        if (environment.equals("local")) {
            Configuration.browser = browser;
            Configuration.startMaximized = true;
            //Configuration.headless = true;
            Configuration.pageLoadStrategy = "eager";
            if (mobile.equals("true")) {
                Configuration.startMaximized = false;
                Configuration.browserSize = "414x736";
                //Configuration.headless = true;
            }
            DesiredCapabilities capabilities = new DesiredCapabilities();
            switch (browser) {
                case "chrome":
                    ChromeOptions options = new ChromeOptions();
                    //options.addArguments("--auto-open-devtools-for-tabs");
                    options.addArguments("--disable-gpu");
                    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    capabilities = DesiredCapabilities.chrome();
                    capabilities.setCapability("profile.default_content_setting_values.notifications", 2);
                    capabilities.setCapability("intl.accept_languages", "en,en_US");
                    capabilities.setCapability("profile.password_manager_enabled", false);
                    //capabilities.setVersion("65.0");
                    //capabilities.setCapability("enableVNC", true);
                    //capabilities.setCapability("enableVideo", true);
                    //capabilities.setPlatform(Platform.LINUX);
                    Configuration.browserCapabilities = capabilities;
                    break;
                case "firefox":
                    capabilities = DesiredCapabilities.firefox();
                    //capabilities.setVersion("57.0");
                    //capabilities.setCapability("enableVNC", true);
                    //capabilities.setPlatform(Platform.LINUX);
                    break;
                default:
                    throw new IllegalStateException("Browser " + browser + " not supported in tests");
            }
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            switch (browser) {
                case "chrome":
                    Configuration.startMaximized = true;
                    ChromeOptions options = new ChromeOptions();
                    Map<String, Object> prefs = new HashMap<String, Object>();
                    prefs.put("profile.default_content_setting_values.notifications", 2);
                    prefs.put("intl.accept_languages", "en,en_US");
                    options.setExperimentalOption("prefs", prefs);
                    options.addArguments("--disable-gpu");
                    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    capabilities = DesiredCapabilities.chrome();
                    //capabilities.setVersion("87.0");
                    capabilities.setCapability("enableVNC", true);
                    capabilities.setCapability("enableVideo", false);
                    capabilities.setCapability("name", testName);
                    capabilities.setCapability("timeZone", "Europe/London");

                    if (mobile.equals("true")) {
                        capabilities.setCapability("screenResolution", "514x736x24");
                    }
                    //capabilities.setPlatform(Platform.LINUX);
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    break;
                case "firefox":
                    capabilities = DesiredCapabilities.firefox();
                    //capabilities.setVersion("73.0");
                    //capabilities.setCapability("enableVNC", true);
                    //capabilities.setPlatform(Platform.LINUX);
                    break;
                default:
                    throw new IllegalStateException("Browser " + browser + " not supported in tests");
            }
            RemoteWebDriver webDriver = new RemoteWebDriver(new URL(selenoidUrl), capabilities);
            if (mobile.equals("true")) {
                webDriver.manage().window().setSize(new Dimension(514, 736));
            } else {
                webDriver.manage().window().setSize(new Dimension(1920, 1080));
            }
            WebDriverRunner.setWebDriver(webDriver);
        }
    }
}
