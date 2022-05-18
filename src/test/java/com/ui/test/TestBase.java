package com.ui.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.core.enums.CornerNotifications;
import com.ui.Init;
import org.testng.Assert;
import static com.codeborne.selenide.Selenide.*;

public class TestBase extends Init {
    public void assertNotificationShown(CornerNotifications cornerNotifications, SelenideElement frameYouWantToBackIn){
        switchTo().defaultContent();
        SelenideElement notificationLocatorByText = $x("//div[@class='aui-flag ac-aui-flag']/div[contains(text(), '"+cornerNotifications.getContent() + "')]").shouldBe(Condition.exist);
        Assert.assertTrue(notificationLocatorByText.isDisplayed(), "Не выводится всплывающее уведомление с содержанием: " + cornerNotifications.getContent());
        switchTo().frame(frameYouWantToBackIn);
    }
//    public void deleteAllReminder(){
//        //перехода на страицу задачи
//        ElementsCollection buttons = $$x("//button[@data-tl-test-id='custom-rem.button-cancel']");
//        //if buttons.size != 0 {
//        SelenideElement noRemindersMessage = $x("//div[@class='reminder-list' and contains(text(), 'No reminders')]");
//        buttons.forEach(SelenideElement::click);
//        Assert.assertTrue(noRemindersMessage.shouldBe(Condition.visible).isDisplayed(), "Не выводится надпись об отсутствии напоминаний в списке");
//    }
}
