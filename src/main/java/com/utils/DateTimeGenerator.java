package com.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class DateTimeGenerator {

    private static final DateTimeFormatter onlyTime = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter onlyDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter onlyHour = DateTimeFormatter.ofPattern("HH");

    private static ZoneId TIMEZONE;

    private static LocalDateTime setDateTime(){
        readPropertiesFile();
        TIMEZONE = ZoneId.of(getProperty("timezone"));
        return LocalDateTime.now(DateTimeGenerator.TIMEZONE);
    }

    public static String getCurrentHour(){
        return setDateTime().format(onlyHour);
    }
    public static String getCurrentTime(){
        return setDateTime().format(onlyTime);
    }

    private static String getMidnightTime(){
        return "00:00";
    }

    public static String getFutureDate(int afterHowManyDays){
        return setDateTime().plusDays(afterHowManyDays).format(onlyDate);
    }

    public static String getPastDate(int beforeHowManyDays){
        return setDateTime().minusDays(beforeHowManyDays).format(onlyDate);
    }

    public static String getFutureHour(int afterHowManyHours){
        return setDateTime().plusHours(afterHowManyHours).format(onlyHour);
    }

    public static String getPastHour(int beforeHowManyHours){
        return setDateTime().minusHours(beforeHowManyHours).format(onlyHour);
    }

    public static String getTonightDateTime(){
        return getFutureDate(1)+ " " +getMidnightTime();
    }

    public static String getTomorrowDateTime(){
        return getFutureDate(1) + " " + getCurrentTime();
    }

    public static String getInAWeekDateTime(){
        return setDateTime().plusWeeks(1).format(onlyDate) + " " + getCurrentTime();
    }

    public static String getInAMonthDateTime(){
        return setDateTime().plusMonths(1).format(onlyDate) + " " + getCurrentTime();
    }
}
