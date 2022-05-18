package com.ui;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.codeborne.selenide.Selenide.*;
import static com.ui.utils.BrowserFactory.setBrowser;
import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class Init {
    @BeforeMethod
    protected void init() {
        readPropertiesFile();
        try {
            setBrowser(getProperty("browser"),
                    getProperty("environment"),
                    getProperty("selenoidUrl"),
                    "",
                    getProperty("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    public static void closeDriver(){
        closeWebDriver();
    }
}
