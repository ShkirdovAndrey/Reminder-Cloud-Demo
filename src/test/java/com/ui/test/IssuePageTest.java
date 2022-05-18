package com.ui.test;

import com.codeborne.selenide.SelenideElement;
import com.core.entities.CreatedReminderInIssueList;
import com.core.entities.GroupAndUserNames;
import com.core.entities.Reminder;
import com.core.entities.User;
import com.core.enums.CornerNotifications;
import com.core.enums.DefaultRemType;
import com.core.enums.ReminderRepeatType;
import com.ui.pages.IssuePage;
import com.ui.pages.ReminderCreatingForm;
import com.ui.pages.maintenance.LoginPage;
import com.ui.pages.maintenance.MainPage;
import com.utils.DateTimeGenerator;
import io.qameta.allure.Issue;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class IssuePageTest extends TestBase {
    public static User user = new User("autobot", "automation.teamlead.bot@gmail.com", "1234567890");
    public static IssuePage issuePage;
    public static MainPage mainPage;
    public static LoginPage loginPage;
    public static GroupAndUserNames names;

    @BeforeGroups
    public void setUp() {
        loginPage = new LoginPage(user);
        mainPage = new MainPage();
        mainPage.getIssue();
        issuePage = new IssuePage();
        names = new GroupAndUserNames();

    }


    @Test(testName = "Проверка интерфейса") //DEV-T1173
    public void uiUxSectionTests() {
        setUp();
        issuePage.assertIsElementsDisplayed();
//                .assertNoRemindersInList();
    }


    @DataProvider(name = "data-provider")
    public Object[][] getValues() {
        return new Object[][]{
                {DefaultRemType.TONIGHT},
                {DefaultRemType.TOMORROW},
                {DefaultRemType.IN_A_WEEK},
                {DefaultRemType.IN_A_MONTH}
                // TODO: 22.03.2022 Съезжают на час относительно других напоминаний. Причины в моем коде не нашел
        };
    }

    //DEV-T1175
    @Test(testName = "Проверка создания дефолтных напоминаний", dataProvider = "data-provider", description = "Проверка создания дефолтных напоминаний")
    public void creatingDefault(DefaultRemType remType) {
        setUp();
        Reminder rem = issuePage.createDefaultReminder(remType, user);
        CreatedReminderInIssueList remLine = new CreatedReminderInIssueList(rem);
        issuePage.assertNotificationRemWasAdded()
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved()
                .assertNoRemindersInList();
    }


    @Test(testName ="Проверка содержания формы создания напоминания" ) //DEV-T1177 DEV-T1179
    public void creatingFormContentIsDisplay() {
        setUp();
        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);
        form
                .assertDefaultFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.DAILY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.MONTHLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.YEARLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.DO_NOT_REPEAT, 0).assertDefaultFormContent()
                .closeForm();
    }

    @Test(testName ="Сценарий: создания одноразового напоминания" ) //DEV-T1177 DEV-T1179
    public void scenarioNonRecurrentReminderCreating() {
        setUp();
        String futureDate = DateTimeGenerator.getFutureDate(2); //дата через пару дней
        String futureHour = DateTimeGenerator.getFutureHour(2); //час через пару часов
        String changedMinutes = "20";
        String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ut eros libero. Vestibulum non semper odio. Cras non lacus quis neque vestibulum molestie. Praesent eget consequat purus, vel sodales sed.";

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);
        Reminder rem = form
                .specifyNextSendDateTime(futureDate, futureHour, changedMinutes)
                .writeMessage(longMessage)
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved();
    }


    @Test(testName ="Сценарий: создание повторяемого напоминания, без окончания повторения" )  //DEV-T1184
    public void scenarioRepeatWithoutEndingReminderCreating() {
        setUp();
        String date = DateTimeGenerator.getFutureDate(1);

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.YEARLY, user);
        Reminder rem = form
                .clearAddressee(user.getUserName()).addAddresseeInField(names.getSITE_ADMIN_NAME())
                .chooseRepeatability(ReminderRepeatType.DAILY)
                .specifyNextSendDateTime(date, "08", "55")
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved();
    }

    @Test(testName ="Сценарий: создание повторяемого напоминания, с окончанием повторения" ) //DEV-T1185
    public void scenarioRepeatWithEndingReminderCreating() {
        setUp();
        String nextSend = DateTimeGenerator.getCurrentHour();
        String endDate = DateTimeGenerator.getFutureDate(30);
        String message = "Сообщение напоминания";

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DAILY, user);

        Reminder rem = form
                .clearAddresseeField().addAddresseeInField(names.getSITE_ADMIN_GROUP())
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY, 2)
                .specifyNextSendDateTime(DateTimeGenerator.getFutureDate(1),nextSend,"00")
                .specifyEndRepeatingDate(endDate)
                .writeMessage(message)
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved();
    }

    @Test(testName ="Ограничения при создании напоминания" ) //DEV-T1189
    public void creatingFormFieldsValidationTests() {
        setUp();
        String pastDate = DateTimeGenerator.getPastDate(1);
        String pastHour = DateTimeGenerator.getPastHour(1);
        String futureDate = DateTimeGenerator.getFutureDate(2);
        String futureHour = DateTimeGenerator.getFutureHour(1);

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);
        SelenideElement frameForm = form.getFormIFrame();

        form.clearAddresseeField()
                .tryConfirmCreation();
        assertNotificationShown(CornerNotifications.EMPTY_ADDRESSEE_ERROR, frameForm);

        form.addAddresseeInField(names.getSITE_ADMIN_GROUP()).chooseRepeatability(ReminderRepeatType.DAILY).clearEveryField();
        form.tryConfirmCreation();
        assertNotificationShown(CornerNotifications.EMPTY_EVERY_FIELD_ERROR, frameForm);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY, 1)
                .clickEndsOnWithoutEndDateSpecify().tryConfirmCreation();
        assertNotificationShown(CornerNotifications.EMPTY_END_DATE_ERROR, frameForm);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.DO_NOT_REPEAT, 0)
                .specifyNextSendDateTime(pastDate, pastHour, "00")
                .tryConfirmCreation();
        assertNotificationShown(CornerNotifications.INCORRECT_WHEN_DATE_ERROR, frameForm);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.DAILY, 1)
                .tryConfirmCreation();
        assertNotificationShown(CornerNotifications.INCORRECT_STARTS_ON_DATE_ERROR, frameForm);

        form.specifyNextSendDateTime(futureDate, futureHour, "00")
                .specifyEndRepeatingDate(pastDate)
                .tryConfirmCreation();
        assertNotificationShown(CornerNotifications.INCORRECT_ENDS_ON_DATE_ERROR, frameForm);

        form.closeForm();
    }

}
