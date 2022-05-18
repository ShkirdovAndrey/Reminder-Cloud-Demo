package com.core.entities;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;
import static com.codeborne.selenide.Selenide.$x;

public class CreatedReminderInIssueList {

    private String addressesList;
    private String creatorName;
    @Getter
    private String nextSendDateTime;
    private String message;
    private boolean isGroupReminder;
    private boolean isRepeatable;

    @Getter
    @Setter
    private SelenideElement addresseeIconLocator;
    @Getter
    @Setter
    private SelenideElement addresseeLocator;
    @Getter
    @Setter
    private SelenideElement nextSendDateTimeLocator;
    @Getter
    @Setter
    private SelenideElement repeatIconLocator;
    @Getter
    @Setter
    private SelenideElement messageLocator;
    @Getter
    @Setter
    private SelenideElement deleteButtonLocator;

    public CreatedReminderInIssueList(Reminder reminder){ //все локаторы будут завязаны на следующей дате отправки, так она у напоминаний преимущественно отличается, и всегда наличествует
        GroupAndUserNames names = new GroupAndUserNames();
        String addresseeLocatorContent = "";
        String additionPartByAddressee = "";

        for(String arrayElement : reminder.getAddressees()) {
            additionPartByAddressee = additionPartByAddressee + " and contains(text(),'"+arrayElement+"')";
            addresseeLocatorContent = "//span[@data-tl-test-id='task-interface.addressees'" +additionPartByAddressee+"]";
        }

        this.creatorName = reminder.getCreatorName();
        this.isGroupReminder = reminder.getAddressees().contains(names.getSITE_ADMIN_GROUP()) || reminder.getAddressees().contains(names.getUSER_GROUP()) || reminder.getAddressees().size()!=1;
        this.isRepeatable = reminder.isRepeat();
        this.nextSendDateTime = reminder.getNextSendDateTime();
        this.message = reminder.getMessage();


        if (isGroupReminder){
            this.addresseeIconLocator = $x("//div[text()='" + nextSendDateTime + "']//ancestor::div[@data-tl-test-id='task-interface.created']//descendant::span[@data-tl-test-id='task-interface.icon-group']");
        }
        else this.addresseeIconLocator = $x("//div[text()='" + nextSendDateTime + "']//ancestor::div[@data-tl-test-id='task-interface.created']//descendant::img[@data-tl-test-id='task-interface.icon-user']");

        this.addresseeLocator = $x(addresseeLocatorContent);
        if (isRepeatable){  this.repeatIconLocator = $x("//div[text()='" + nextSendDateTime + "']/img");}

        this.nextSendDateTimeLocator = $x("//div[text()='" + nextSendDateTime + "' and @data-tl-test-id='task-interface.datetime-send']");
        this.messageLocator = $x("//div[text()='" + nextSendDateTime + "']//ancestor::div[@data-tl-test-id='task-interface.created']//descendant::div[@class='rem-long-text' and contains(text(),'"+message+"')]");
        this.deleteButtonLocator =  $x("//div[text()='" + nextSendDateTime + "' and @data-tl-test-id='task-interface.datetime-send']/parent::span/following-sibling::button[@data-testid='close-button-task-interface.delete-rem']");

        // TODO: 18.02.2022 Дописать локаторы для сообщения,а также доделать вызов этих локаторов конкретного объекта для его поиска в списке;
    }


}
