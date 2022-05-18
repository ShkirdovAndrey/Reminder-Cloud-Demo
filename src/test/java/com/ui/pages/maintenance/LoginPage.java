package com.ui.pages.maintenance;

import com.codeborne.selenide.Condition;
import com.core.entities.User;
import com.codeborne.selenide.SelenideElement;
import com.utils.Waiter;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.utils.Waiter.waitForElementAppears;


public class LoginPage extends PageBase {

    @Getter
    private final SelenideElement FORM = $$("h5").findBy(or(
            "",
            text("Войдите в свой аккаунт"),
            text("Log in to your account")
    ));
    private final SelenideElement loginField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement submitButton = $("#login-submit");

    private final String loginUrl = "https://id.atlassian.com/login?continue=https%3A%2F%2Fteamlead-instance2.atlassian.net%2Flogin%3FredirectCount%3D1%26application%3Djira&application=jira";

    public LoginPage() {
        open(loginUrl);
        FORM.shouldBe(visible);
    }

    public LoginPage(User user) {
        open(loginUrl);
        Waiter.waitForElementAppears(FORM);
        fillAndSendData(user);
    }

    @Step("Залогиниться как юзер {0}")
    public LoginPage fillAndSendData(User user) {
        loginField.sendKeys(user.getEmail());
        submitButton.click();
        Waiter.waitForElementAppears(passwordField);
        passwordField.sendKeys(user.getPassword());
        submitButton.click();
        successLogin();
        return this;
    }

    @Step("Проверить что мы попали на MainPage")
    public LoginPage successLogin() {
        SelenideElement head = $$x(".//h1").findBy(Condition.text("Your work"));
        waitForElementAppears(head);
        return this;
    }
}
