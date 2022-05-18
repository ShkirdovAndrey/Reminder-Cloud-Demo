package com.core.enums;

import lombok.Getter;

public enum ReminderRepeatType {
    DO_NOT_REPEAT("Don't repeat", false),
    DAILY("Daily", true),
    WEEKLY("Weekly",true),
    MONTHLY("Monthly",true),
    YEARLY("Yearly",true);

    @Getter
    private final String value;
    @Getter
    private final boolean isRepeat;

    ReminderRepeatType(String status, boolean isRepeat) {
        this.value = status;
        this.isRepeat = isRepeat;
    }

    public static ReminderRepeatType getTypeByName(String value) {
        for (ReminderRepeatType v : values()) {
            if (v.value.replaceAll("(\\s+|_)", "").toLowerCase().contains(value.replaceAll("(\\s+|_)", "").toLowerCase())) {
                return v;
            }
            if(value.toLowerCase().contains(v.value.toLowerCase())){
                return v;
            }
        }
        throw new EnumConstantNotPresentException(ReminderRepeatType.class, value);
    }
}
