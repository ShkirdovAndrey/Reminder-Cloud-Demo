package com.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.core.entities.CreatedReminderInIssueList;
import com.core.entities.Reminder;
import com.core.entities.User;
import com.core.enums.CornerNotifications;
import com.core.enums.DefaultRemType;
import com.core.enums.ReminderRepeatType;
import com.ui.pages.maintenance.PageBase;
import com.utils.Waiter;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class IssuePage extends PageBase {
    //все кастомные локаторы элементов перечислены на странице: https://wiki.teamlead.ru/pages/viewpage.action?pageId=237441450
    public static SelenideElement reminderInIssueFrame = $x(".//iframe[contains(@id, 'ru.teamlead.jira.plugins.reminder-for-jira.test__reminder-issue-content-panel')]");

    private final SelenideElement customButton = $x("//button[@data-tl-test-id='task-interface.button-custom']");
    private final SelenideElement tonightButton = $x("//button[@data-tl-test-id='task-interface.button-tonight']");
    private final SelenideElement tomorrowButton = $x("//button[@data-tl-test-id='task-interface.button-tomorrow']");
    private final SelenideElement inAWeekButton = $x("//button[@data-tl-test-id='task-interface.button-week']");
    private final SelenideElement inAMonthButton = $x("//button[@data-tl-test-id='task-interface.button-month']");

    private final SelenideElement timeZoneInfo = $x("//a[@data-tl-test-id='task-interface.timezone']");

    private final SelenideElement noRemindersMessage = $x("//div[@class='reminder-list' and contains(text(), 'No reminders')]");

    public IssuePage() {
        Waiter.waitForElementAppears(reminderInIssueFrame);
        switchToFrame(reminderInIssueFrame);
    }

    public Reminder createDefaultReminder(DefaultRemType type, User user) {
        switch (type) {
            case TONIGHT:
                tonightButton.shouldBe(Condition.visible).click();
                break;
            case TOMORROW:
                tomorrowButton.shouldBe(Condition.visible).click();
                break;
            case IN_A_WEEK:
                inAWeekButton.shouldBe(Condition.visible).click();
                break;
            case IN_A_MONTH:
                inAMonthButton.shouldBe(Condition.visible).click();
                break;
        }
        return new Reminder(type, user);
    }

    public CreatedReminderInIssueList createReminderLineInIssue(Reminder reminder) {
        return new CreatedReminderInIssueList(reminder);
    }

    public ReminderCreatingForm callNewReminderCreatingRForm(ReminderRepeatType remainderRepeatType, User creator) {
        customButton.shouldBe(Condition.visible).click();
        switchToDefaultContent();
        return new ReminderCreatingForm(remainderRepeatType, creator);
    }

    public IssuePage assertRemWasCreated(Reminder reminder) {
        CreatedReminderInIssueList rem = createReminderLineInIssue(reminder);
        Assert.assertTrue(rem.getAddresseeIconLocator().shouldBe(Condition.visible).isDisplayed(), "Корректная иконка адресата/адресатов не выводится или не соответствует типу напоминания");
        Assert.assertTrue(rem.getAddresseeLocator().shouldBe(Condition.visible).isDisplayed(), "Адресаты/адресат напоминания неверный или не выводится");
        Assert.assertTrue(rem.getNextSendDateTimeLocator().shouldBe(Condition.visible).isDisplayed(), "Дата напоминания неверная или не выводится");
        Assert.assertTrue(rem.getDeleteButtonLocator().shouldBe(Condition.visible).isDisplayed(), "Не выводится кнопка удаления");
        Assert.assertEquals(rem.getMessageLocator().shouldBe(Condition.exist).getText(), reminder.getMessage(), "Не выводится сообщение напоминания или содержание не соответствует");

        if (reminder.isRepeat()) {
            Assert.assertTrue(rem.getRepeatIconLocator().isDisplayed(), "Не выводится иконка повторяемости");
        }
        return this;
    }

    public IssuePage assertIsElementsDisplayed() {
        Waiter.waitForElementAppears(customButton);
        Assert.assertTrue(customButton.shouldBe(Condition.visible).isDisplayed(), "Кнопка создания кастомного напоминания \"Reminder\" не выводится");
        Assert.assertTrue(tonightButton.shouldBe(Condition.visible).isDisplayed(), "Кнопка создания полночного напоминания \"Tonight\" не выводится");
        Assert.assertTrue(tomorrowButton.shouldBe(Condition.visible).isDisplayed(), "Кнопка создания завтрашнего напоминания \"Tomorrow\"не выводится");
        Assert.assertTrue(inAWeekButton.shouldBe(Condition.visible).isDisplayed(), "Кнопка создания кастомного напоминания \"In a Week\"не выводится");
        Assert.assertTrue(inAMonthButton.shouldBe(Condition.visible).isDisplayed(), "Кнопка создания кастомного напоминания \"In a Month\"не выводится");
        Assert.assertTrue(timeZoneInfo.shouldBe(Condition.visible).isDisplayed(), "Таймзона не выводится");

        return this;
    }

    public IssuePage assertNotificationRemWasAdded() {
        assertNotificationShown(CornerNotifications.REMINDER_CREATED_NOTIFICATION, reminderInIssueFrame);
        return this;
    }

    public IssuePage assertNotificationWasRemoved() {
        assertNotificationShown(CornerNotifications.REMINDER_REMOVED_NOTIFICATION, reminderInIssueFrame);
        return this;
    }

    public void assertNoRemindersInList() {
        Assert.assertTrue(noRemindersMessage.shouldBe(Condition.visible).isDisplayed(), "Не выводится надпись об отсутствии напоминаний в списке");
    }

    public String getTimeZone() {
        return timeZoneInfo.shouldBe(Condition.visible).getText();
    }

    public IssuePage deleteReminder(CreatedReminderInIssueList remLine) {
        remLine.getDeleteButtonLocator().click();
        return this;
    }


}
