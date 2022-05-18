package com.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ReminderFormEntity {
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

    public ReminderFormEntity(){};

    public ReminderFormEntity(String creatorName, ArrayList<String> addresseesList, String repeatType, int repetitionRate, String dateTime, boolean isEnds, String endDate, String message) {
        this.creatorName = creatorName;
        this.addresseesList = addresseesList;
        this.repeatType = repeatType;
        this.repetitionRate = repetitionRate;
        this.dateTime = dateTime;
        this.isEnds = isEnds;
        this.endDate = endDate;
        this.message = message;
    }




}
