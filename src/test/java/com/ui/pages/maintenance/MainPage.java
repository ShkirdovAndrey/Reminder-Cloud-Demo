package com.ui.pages.maintenance;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage extends PageBase {

    private final SelenideElement pageTitle = $$x(".//h1").findBy(Condition.text("Your work"));

    private final String mainUrl = "https://teamlead-instance2.atlassian.net/jira/your-work";
    private final String issueUrl = "https://teamlead-instance2.atlassian.net/browse/REM-1";

    public MainPage(){
        open(mainUrl);
/*        try {
            Thread.sleep(1500); // TODO: 24.02.2022 от этого надо избавиться, но лучше просто перейти на API-логин
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        pageTitle.shouldBe(Condition.visible, Duration.ofSeconds(6));
    }

    @Step("Переход на страницу задачи с плагином")
    public MainPage getIssue(){
        open(issueUrl);
        return this;
    }

    @Step("Провали в фрейм ремайндера")
    public MainPage intoReminder(){
        switchToReminderFrame();
        return this;
    }
}
