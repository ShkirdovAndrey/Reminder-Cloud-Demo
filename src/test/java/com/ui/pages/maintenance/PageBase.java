package com.ui.pages.maintenance;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.core.enums.CornerNotifications;
import com.ui.Init;
import com.ui.pages.IssuePage;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.testng.Assert;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;

public class PageBase {
    public static SelenideElement clearAndFillTextField(SelenideElement field, String value) {
        field.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        field.shouldBe(Condition.visible).val(value);
        return field;
    }

    @Step("Open new tab and switch to tab")
    public static void openNewTab() {
        executeJavaScript("window.open();");
        switchToNewTab();
    }

    @Step("Switch to tab")
    public static void switchToTab(Integer index) {
        switchTo().window(index);
    }

    @Step("Upload file")
    public static void uploadFile(String inputLocator, String pathToFile) {
        try {
            String fullPath = new File(pathToFile).getCanonicalPath();
            $x(inputLocator).sendKeys(fullPath);
        } catch (Exception e) {
            throw new AssertionError("Path wan't parsed correctly. Exception: \n" + e.getMessage());
        }
    }

    @Step("Switch to new tab")
    public static void switchToNewTab() {
        int countOfTabs = getCountOfOpenedWindows();
        switchToTab(countOfTabs - 1);
    }

    @Step("Close Window")
    public static void closeLastOpenedWindow() {
        int count = getCountOfOpenedWindows();
        switchToTab(count - 1);
        getWebDriver().close();
    }

    @Step("Close all tabs from last to first")
    public static void closeExtraTabs(int count) {
        for (int i = 0; i < count; i++) {
            closeLastOpenedWindow();
            switchToNewTab();
        }
    }

    @Step("Get number of opened windows")
    public static int getCountOfOpenedWindows() {
        return getWebDriver().getWindowHandles().size();
    }

    @Step("Switch to Frame")
    public void switchToFrame(String nameOrId) {
        switchTo().frame(nameOrId);
    }

    @Step("Switch to Frame")
    public void switchToFrame(SelenideElement frame) {
        switchTo().frame(frame);
    }

    @Step("Switch to Default Content")
    public void switchToDefaultContent() {
        switchTo().defaultContent();
    }

    @Step("Switch to Reminder Frame in Issue ")
    public void switchToReminderFrame(){
        switchToFrame(IssuePage.reminderInIssueFrame.shouldBe(Condition.exist));
    }

    @Step("Scroll to Element by center")
    public static void scrollToElement(SelenideElement element) {
        Point p = element.getLocation();
        executeJavaScript("window.scroll(" + p.getX() + "," + (p.getY() - 400) + ");");
    }

    @Step("Scroll to Element view")
    public static void scrollToElementView(SelenideElement element) {
        executeJavaScript("arguments[0].scrollIntoView();", element);
    }

    @Step("Click OK on modal popup")
    public static void clickOkOnModalPopup() {
        confirm();
    }

    public static String getUrl() {
        return url();
    }

    public static String getFormattedCookies() {
        Set<Cookie> cookies = WebDriverRunner.getWebDriver().manage().getCookies();
        return cookies.stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining("; "));
    }
    public void assertNotificationShown(CornerNotifications cornerNotifications, SelenideElement frameYouWantToBackIn){
        switchTo().defaultContent();
        SelenideElement notificationLocatorByText = $x("//div[@class='aui-flag ac-aui-flag']/div[contains(text(), '"+cornerNotifications.getContent() + "')]").shouldBe(Condition.exist);
        Assert.assertTrue(notificationLocatorByText.isDisplayed(), "Не выводится всплывающее уведомление с содержанием: " + cornerNotifications.getContent());
        switchTo().frame(frameYouWantToBackIn);
    }

}
