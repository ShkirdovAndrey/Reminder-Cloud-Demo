package com.core.enums;

import lombok.Getter;

public enum CornerNotifications {
    EMPTY_ADDRESSEE_ERROR("At least one user should be selected."),
    EMPTY_EVERY_FIELD_ERROR("\"Every\" field must be set"),
    EMPTY_END_DATE_ERROR("Select the end date."),
    INCORRECT_WHEN_DATE_ERROR("\"When\" field has an error. Please check that the date and time is greater than the current date and time."),
    INCORRECT_STARTS_ON_DATE_ERROR("\"Starts On\" field has an error. Please check that the date and time is greater than the current date and time."),
    INCORRECT_ENDS_ON_DATE_ERROR("The End Date is earlier than the next reminder dispatch date."),
    NEED_RESET_STARTS_ON_DATE_ERROR("Cannot create a reminder for the past period."),
    REMINDER_CREATED_NOTIFICATION("A new reminder was added"),
    REMINDER_REMOVED_NOTIFICATION("The reminder was removed");

    @Getter
    private String content;
    CornerNotifications(String content){
        this.content = content;
    }
}
