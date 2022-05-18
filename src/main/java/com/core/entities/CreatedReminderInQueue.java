package com.core.entities;

import lombok.Getter;

import java.util.ArrayList;

import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class CreatedReminderInQueue {
    @Getter
    private final String issueKey;
    @Getter
    private final  String summary;
    @Getter
    private final  ArrayList<String> addressees;
    @Getter
    private final  String message;
    @Getter
    private final  String creator;
    @Getter
    private final  String nextSend;
    @Getter
    private final  String recurrence;

    public CreatedReminderInQueue(Reminder reminder) {
        issueKey = generateIssueKey();
        summary = getSummaryFromProperties();
        addressees = reminder.getAddressees();
        message = generateMessage(reminder);
        creator = reminder.getCreatorName();
        nextSend = reminder.getNextSendDateTime();
        recurrence = generateRecurrenceString(reminder);

    }

    public String generateRecurrenceString(Reminder reminder){
        String endDate = "";
        String period;
        String rate = String.valueOf(reminder.getRepetitionRate());
        if(!reminder.isRepeat()){
            return "non-recurrent";
        }
        else if(reminder.isHaveEndDate()){
            endDate = " till "+reminder.getEndsOn();
        }
        switch (reminder.getRepeatType()){
            case("Daily"):
                period = "day";
                break;
            case("Weekly"):
                period = "week";
                break;
            case("Monthly"):
                period = "month";
                break;
            case("Yearly"):
                period = "year";
                break;
            default:
                period = "";
        }
        if (reminder.getRepetitionRate()!= 1){
            period = period+"s";
        }

        return "Each "+ rate + " " + period + " from "+ reminder.getNextSendDateTime() + endDate;
    }

    public String generateIssueKey(){
        readPropertiesFile();
        String issueUrl = getProperty("issueUrl");
        String startChar = "browse/";
        int startIndex = issueUrl.indexOf(startChar)+ startChar.length();
        return issueUrl.substring(startIndex);

    }

    public String getSummaryFromProperties(){
        readPropertiesFile();
        return getProperty("issueSummary");
    }

    public String generateMessage(Reminder reminder){
        if (reminder.getMessage().isEmpty()){
            return "Default system message";
        }
        else return reminder.getMessage();
    }
}
