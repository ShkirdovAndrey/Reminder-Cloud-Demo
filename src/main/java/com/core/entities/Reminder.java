package com.core.entities;

import com.core.enums.DefaultRemType;
import com.core.enums.ReminderRepeatType;
import com.utils.DateTimeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Reminder {
    @Getter
    @Setter
    private String creatorName;

    @Getter
    @Setter
    private ArrayList<String> addressees;

    @Getter
    @Setter
    private boolean repeat;

    @Getter
    @Setter
    private String nextSendDateTime;

    @Getter
    @Setter
    private String repeatType;

    @Getter
    @Setter
    private int repetitionRate;

    @Getter
    @Setter
    private boolean haveEndDate;

    @Getter
    @Setter
    private String endsOn;

    @Getter
    @Setter
    private String message;

    public Reminder() {

    }

    public Reminder(String creatorName, ArrayList<String> addressees, boolean repeat, String date, String reminderRepeatType, int repetitionRate, boolean haveEndDate, String endsOn, String message) {
        this.creatorName = creatorName;
        this.addressees = addressees;
        this.repeat = repeat;
        this.nextSendDateTime = date;
        this.repeatType = reminderRepeatType;
        this.repetitionRate = repetitionRate;
        this.haveEndDate = haveEndDate;
        this.endsOn = endsOn;
        this.message = message;
    }

    public Reminder(DefaultRemType type, User user) {

        this.creatorName = user.getUserName();
        ArrayList<String> addresseesList = new ArrayList<>();
        addresseesList.add(user.getUserName());
        this.addressees = addresseesList;
        this.repeat = false;

        switch (type) {
            case TONIGHT:
                nextSendDateTime = DateTimeGenerator.getTonightDateTime();
                break;
            case TOMORROW:
                nextSendDateTime = DateTimeGenerator.getTomorrowDateTime();
                break;
            case IN_A_WEEK:
                nextSendDateTime = DateTimeGenerator.getInAWeekDateTime();
                break;
            case IN_A_MONTH:
                nextSendDateTime = DateTimeGenerator.getInAMonthDateTime();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        this.repeatType = ReminderRepeatType.DO_NOT_REPEAT.getValue();
        this.repetitionRate = 0;
        this.haveEndDate = false;
        this.endsOn = "";
        this.message = "";
    }

}
