package com.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.core.entities.Reminder;
import com.core.entities.User;
import com.core.enums.CornerNotifications;
import com.core.enums.ReminderRepeatType;
import com.ui.pages.maintenance.PageBase;
import com.utils.DateTimeGenerator;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.core.enums.ReminderRepeatType.DO_NOT_REPEAT;

public class ReminderCreatingForm extends PageBase {
    @Getter
    private final SelenideElement formIFrame = $x("//iframe[contains(@id, 'ru.teamlead.jira.plugins.reminder-for-jira.test__dialog-create-reminder')]");

    private final SelenideElement formHeader = $x("//h2[text()='Reminder']");
    private final SelenideElement addresseeHeader = $x("//label[text()='To']");
    private final SelenideElement endsHeaderLocator = $x("//span[text()='Ends']");
    private final SelenideElement endsRadioBlock = $x("//div[@data-tl-test-id='custom-rem.radio-block']");
    private final SelenideElement messageHeader = $x("//label[text()='Message']");

    private final SelenideElement createButton = $x("//button[@data-tl-test-id='custom-rem.button-create']");
    private final SelenideElement closeButton = $x("//button[@data-tl-test-id='custom-rem.button-cancel']");

    private final SelenideElement addressesField = $x("//div[contains(@class, 'custom-rem-addressees')]/child::div");
    private final SelenideElement addressesFieldClearButtonInactive = $x("//div[contains(@class, 'select__clear-indicator')]//span[@aria-label='clear']");

    private final SelenideElement repeatSelector = $x("//div[contains(@class, 'custom-rem.repeat-selector')]");

    private String textInLocatorByRepeatType(ReminderRepeatType reminderRepeatType) {
        String result = "";
        switch (reminderRepeatType) {
            case DO_NOT_REPEAT:
                result = "Repeat";
                break;
            case DAILY:
                result = "Daily";
                break;
            case WEEKLY:
                result = "Weekly";
                break;
            case MONTHLY:
                result = "Monthly";
                break;
            case YEARLY:
                result = "Yearly";
                break;
        }
        return result;
    }

    private SelenideElement selectedRepeatLocator(ReminderRepeatType reminderRepeatType) {
        return $x("//div[contains(@class, 'custom-rem.repeat-selector')]//div[contains(text(),'" + textInLocatorByRepeatType(reminderRepeatType) + "')]");
    }

    private SelenideElement repeatOptionLocator(ReminderRepeatType reminderRepeatType) {
        return $x("//*[contains(@data-tl-test-id, 'custom-rem.repeat-selector-option') and contains(text(), '" + textInLocatorByRepeatType(reminderRepeatType) + "')]");
    }

    private final SelenideElement repeatPeriodField = $x("//input[@data-testid='custom-rem.period']");

    private final SelenideElement radioNeverOption = $x("//input[@data-testid='custom-rem.radio-never--radio-input']");
    private final SelenideElement radioOnOption = $x("//input[@data-testid='custom-rem.radio-on--radio-input']");
    private final SelenideElement endsDateField = $x("//div[@data-testid='custom-rem.end-date--container']");
    private final SelenideElement clearEndsFieldButton = $x("//div[@data-testid='custom-rem.end-date--container']//span[@aria-label='clear']");

    private final SelenideElement nextSendDateField = $x("//div[@data-testid='custom-rem.date--container']");
    private final SelenideElement nextSendHourSelector = $x("//div[contains(@class,'custom-rem.hours')]");

    private SelenideElement hoursOptionSelector(String hoursInHHFormat) {
        return $x("//div[contains(@data-tl-test-id, 'custom-rem.hours-option') and contains(text()," + hoursInHHFormat + ")]");
    }

    private SelenideElement selectedHoursLocator(String hoursInHHFormat) {
        return $x("//div[contains(@class,'custom-rem.hours')]//div[contains(text(), " + hoursInHHFormat + ")]");
    }

    private final SelenideElement nextSendMinutesSelector = $x("//div[contains(@class, 'custom-rem.minutes')]");

    private SelenideElement minutesOptionSelector(String minutesInmmFormat) {
        return $x("//div[contains(@data-tl-test-id, 'custom-rem.minutes-option') and contains(text()," + minutesInmmFormat + ")]");
    }

    private SelenideElement selectedMinutesLocator(String minutesInmmFormat) {
        return $x("//div[contains(@class,'custom-rem.minutes')]//div[contains(text(), " + minutesInmmFormat + ")]");
    }

    private final SelenideElement messageField = $x("//textarea[@data-testid='custom-rem.message']");


    @Getter
    @Setter
    private String creatorName;

    @Setter
    @Getter
    private ArrayList<String> addresseesList;

    @Setter
    @Getter
    private String repeatType;

    @Getter
    @Setter
    private int repetitionRate;

    @Getter
    @Setter
    private String dateTime;

    @Getter
    @Setter
    private boolean isEnds;

    @Setter
    @Getter
    private String endDate;

    @Getter
    @Setter
    String message;


    public ReminderCreatingForm(ReminderRepeatType repeatType, User user) {
        actions().pause(1600).perform();     //TODO убрать паузу когда Юра поправит форму
        switchToFrame(formIFrame.shouldBe(Condition.exist));
        addresseeHeader.shouldBe(Condition.visible);
        setCreatorName(user.getUserName());
        addresseesList = new ArrayList<>();
        addresseesList.add(user.getUserName());
        setRepeatType(repeatType.getValue());

        if (repeatType == DO_NOT_REPEAT) {
            selectedRepeatLocator(DO_NOT_REPEAT).shouldBe(Condition.visible);
        } else {
            specifyRepeatabilityAndPeriod(repeatType, 1);
            endsHeaderLocator.shouldBe(Condition.visible);
            endsRadioBlock.shouldBe(Condition.visible);
        }
        setEnds(false);
        setEndDate("");

        nextSendDateField.shouldBe(Condition.visible);
        nextSendHourSelector.shouldBe(Condition.visible);
        nextSendMinutesSelector.shouldBe(Condition.visible);
        setDateTime(DateTimeGenerator.getFutureDate(1) + " " + DateTimeGenerator.getCurrentHour());//Default

        messageHeader.shouldBe(Condition.visible);
        setMessage("");
    }

    // TODO: 06.04.2022 надо добавить на действия Steps с подписью. Они были здесь раньше, но пропали изза неправильного слияния веток
    public ReminderCreatingForm assertDefaultFormContent() {
        SelenideElement addresseeValue = $x("//div[contains(@class, 'custom-rem-addressees')]/child::div[1]/child::div[1]/child::div[1]/child::div[1]/child::div[1]");
        SelenideElement dateValue = $x("//div[@data-testid='custom-rem.date--container']/child::div/child::div/child::div/child::div[1]");
        SelenideElement hourValue = $x("//div[contains(@class, 'custom-rem.hours')]/div[1]/div[1]/div[1]");
        SelenideElement minutesValue = $x("//div[contains(@class, 'custom-rem.minutes')]/div[1]/div[1]/div[1]");
        SelenideElement repeatHeader = $x("//label[text()='Repeat']");
        SelenideElement whenHeader = $x("//label[text()='When']");

        Assert.assertTrue(formHeader.isDisplayed(), "Не выводится заголовок формы");
        Assert.assertTrue(addresseeHeader.isDisplayed(), "Не выводится заголовок поля адресатов или самое поле");
        Assert.assertEquals(creatorName, addresseeValue.getText(), "Не выводится адресат-создатель или выводится неверный адресат");
        Assert.assertTrue(repeatHeader.isDisplayed(), "Не выводится заголовок поля повторяемости");
        Assert.assertTrue(selectedRepeatLocator(DO_NOT_REPEAT).isDisplayed(), "Не выставлен дефолтный вариант повторяемости напоминания");
        Assert.assertTrue(whenHeader.isDisplayed(), "Не выводится заголовок поля даты отправки");
        Assert.assertEquals(DateTimeGenerator.getFutureDate(1), dateValue.getText(), "Выводится неверная дата в поле даты");
//        Assert.assertEquals(DateTimeGen.getFutureHour(0), hourValue.getText(), "Выводится неверный час в поле часа"); todo настройка дефолтного часа зависит от ОС пользователя, а не таймзоны
        Assert.assertEquals("00", minutesValue.getText(), "Выводится неверные минуты в поле минут");
        Assert.assertTrue(messageHeader.isDisplayed(), "Не выводится заголовок поля сообщения");
        Assert.assertTrue(createButton.isDisplayed(), "Не выводится кнопка создания");
        Assert.assertTrue(closeButton.isDisplayed(), "Не выводится кнопка отмены");
        Assert.assertEquals("A short reminder message (optional, max. 255 symbols)", messageField.getAttribute("placeholder"), "Не выводится плейсхолдер/содержание плейсхолдера некорректно");

        return this;
    }

    public ReminderCreatingForm assertRepeatingFormContent() {
        SelenideElement everyHeader = $x("//span[text()='Every']");
        String text = "";
        switch (repeatType) {
            case "Daily":
                text = "day(s)";
                break;
            case "Weekly":
                text = "week(s)";
                break;
            case "Monthly":
                text = "month(s)";
                break;
            case "Yearly":
                text = "year(s)";
                break;
            // TODO: 24.02.2022 Можно оставить так, можно unexpected исключение кинуть.
        }

        SelenideElement periodLine = $x("//span[text()='" + text + "']");
        SelenideElement startsOnHeader = $x("//label[text()='Starts On']");

        Assert.assertTrue(everyHeader.isDisplayed(), "Не выводится надпись Every между полями настройки повторяемости и периодичности");
        Assert.assertTrue(periodLine.isDisplayed(), "Не выводится надпись " + text + " после поля указания периодичности");
        Assert.assertTrue(startsOnHeader.isDisplayed(), "Не выводится заголовок Starts On или он не поменялся с When");
        Assert.assertTrue(endsHeaderLocator.isDisplayed(), "Не выводится заголовок настройки окончания повторения Ends");
        Assert.assertTrue(endsRadioBlock.isDisplayed(), "Не выводится блок радиокнопок настройки окончания повторения");

        return this;
    }

    public Reminder confirmCreation() {
        createButton.shouldBe(Condition.visible).click();
        actions().pause(300).perform(); //TODO Убрать
        formHeader.shouldNotBe(Condition.visible);
        switchToDefaultContent();
        new IssuePage(); //это нужно для перехода в нужный фрейм
        return createCustomReminder();
    }

    public ReminderCreatingForm tryConfirmCreation() {
        createButton.shouldBe(Condition.visible).click();
        return this;
    }

    public void closeForm() {
        closeButton.shouldBe(Condition.visible).click();
        new IssuePage(); //это нужно для перехода в нужный фрейм
    }

    public Reminder createCustomReminder() {
        boolean repeat;
        repeat = !getRepeatType().equals("Don't repeat");
        boolean haveEndDate;
        haveEndDate = !getRepeatType().equals("Don't repeat") || !isEnds();

        return new Reminder(getCreatorName(), getAddresseesList(), repeat, getDateTime(), getRepeatType(), getRepetitionRate(), haveEndDate, getEndDate(), getMessage());
    }
//==============================================ADDRESSEE METHODS ======================

    public ReminderCreatingForm clearAddressee(String addressee) {
        SelenideElement addresseeLocator = $x("//div[contains(text(), '" + addressee + "')]/parent::div//span[@aria-label='Clear']");
        addresseeLocator.shouldBe(Condition.visible).click();
        addresseesList.remove(addressee);
        return this;
    }

    public ReminderCreatingForm clearAddresseeField() {
        addressesFieldClearButtonInactive.click();
        addresseesList.clear();
        return this;
    }

    //TODO Лучше бы избавиться от паузы и научить код видеть выпадающий список адресатов и нужного адресата в нём
    public ReminderCreatingForm addAddresseeInField(String addressee) {
        Selenide.actions().moveToElement(addressesField.shouldBe(Condition.visible)).click()
                .sendKeys(addressee).pause(2000)
                .sendKeys(Keys.ENTER)
                .release()
                .sendKeys(Keys.TAB) //сброс фокуса с поля
                .perform();
        addresseesList.add(addressee);
        return this;
    }
    //==============================================REPEATING SETTINGS METHODS ======================

    public ReminderCreatingForm chooseRepeatability(ReminderRepeatType remainderRepeatType) {
        repeatSelector.shouldBe(Condition.visible).click();
        repeatOptionLocator(remainderRepeatType).shouldBe(Condition.visible).click();
        selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
        repeatPeriodField.shouldBe(Condition.visible);
        setRepeatType(remainderRepeatType.getValue());
        return this;
    }

    public void clearEveryField() {
        repeatPeriodField.sendKeys(Keys.BACK_SPACE);

    }

    public void fillEveryPeriod(int repetitionRate) {
        String rate = Integer.toString(repetitionRate);
        clearEveryField();
        repeatPeriodField.sendKeys(rate);
        setRepetitionRate(repetitionRate);
    }

    public ReminderCreatingForm specifyRepeatabilityAndPeriod(ReminderRepeatType remainderRepeatType, int repetitionRate) {
        repeatSelector.shouldBe(Condition.visible).click();
        repeatOptionLocator(remainderRepeatType).shouldBe(Condition.visible).click();
        setRepeatType(remainderRepeatType.getValue());

        if (remainderRepeatType == DO_NOT_REPEAT) {
            selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
        } else {
            selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
            repeatPeriodField.shouldBe(Condition.visible);
            fillEveryPeriod(repetitionRate);
        }
        return this;
    }
    //==============================================NEXT SEND DATE METHODS ======================

    public ReminderCreatingForm fillNextDateInForm(String dateDDMMYYYY) { //дата в формате, зависящем от настроек окружения; на тестовом установлен формат DD/MM/YYYY
        Selenide.actions().moveToElement(nextSendDateField).click(nextSendDateField).sendKeys(dateDDMMYYYY).sendKeys(Keys.ENTER).release().perform();
        return this;
    }

    public ReminderCreatingForm chooseHoursInForm(String hoursInHHFormat) {
        nextSendHourSelector.click();
        hoursOptionSelector(hoursInHHFormat).shouldBe(Condition.visible).click();
        selectedHoursLocator(hoursInHHFormat).shouldBe(Condition.visible);
        return this;
    }

    public ReminderCreatingForm chooseMinutesInForm(String minutesInmmFormat) { //принимает только минуты с пятиминутным интервалом! начиная с 00 и до 55.
        nextSendMinutesSelector.click();
        minutesOptionSelector(minutesInmmFormat).shouldBe(Condition.visible).click();
        selectedMinutesLocator(minutesInmmFormat).shouldBe(Condition.visible);
        return this;
    }

    public ReminderCreatingForm specifyNextSendDateTime(String dateDDMMYYYY, String hoursInHHFormat, String minutesInmmFormat) {
        fillNextDateInForm(dateDDMMYYYY);
        chooseHoursInForm(hoursInHHFormat);
        chooseMinutesInForm(minutesInmmFormat);
        setDateTime(dateDDMMYYYY, hoursInHHFormat, minutesInmmFormat);
        return this;
    }

    public void setDateTime(String dateDDMMYYYY, String hoursInHHFormat, String minutesInmmFormat) {
        this.dateTime = dateDDMMYYYY + " " + hoursInHHFormat + ":" + minutesInmmFormat;
    }

    //==============================================REPEAT END SETTINGS METHODS ======================

    public void chooseNeverEnds() {
        radioNeverOption.click();
    }

    private void chooseEndsOn() {
        radioOnOption.click();
    }

    public ReminderCreatingForm clickEndsOnWithoutEndDateSpecify() {
        chooseEndsOn();
        return this;
    }

    private void clearEndDateField() {
        clearEndsFieldButton.click();
    }

    public ReminderCreatingForm specifyEndRepeatingDate(String dateDDMMYYYY) {
        chooseEndsOn();
        Selenide.actions().moveToElement(endsDateField).click(endsDateField).sendKeys(dateDDMMYYYY).sendKeys(Keys.ENTER).release().perform();
        setEnds(true);
        setEndDate(dateDDMMYYYY);
        return this;
    }

    public ReminderCreatingForm setNeverEndDate() {
        chooseNeverEnds();
        setEnds(false);
        setEndDate("");
        return this;
    }

    public ReminderCreatingForm writeMessage(String message) {
        messageField.shouldBe(Condition.visible).sendKeys(message);
        setMessage(message);
        return this;

    }

    public void assertFormErrorShown(CornerNotifications cornerNotifications) {
        assertNotificationShown(cornerNotifications, formIFrame);
    }

}
