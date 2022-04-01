package com.example.safemedicare;

public class Medication {
    private String id, user_name,  medicineName, numberOfTime ,doseAmountNumber  , doseAmountText , duration , textDurationSpin , saturday , sunday , monday , tuesday , wednesday ,thursday  , friday , startDayDate , timeH ,timeM  , everyH   ;

    public Medication() {
    }

    public Medication(String id, String user_name, String medicineName, String numberOfTime, String doseAmountNumber, String doseAmountText, String duration, String textDurationSpin, String saturday, String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String startDayDate, String timeH, String timeM, String everyH) {
        this.id = id;
        this.user_name = user_name;
        this.medicineName = medicineName;
        this.numberOfTime = numberOfTime;
        this.doseAmountNumber = doseAmountNumber;
        this.doseAmountText = doseAmountText;
        this.duration = duration;
        this.textDurationSpin = textDurationSpin;
        this.saturday = saturday;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.startDayDate = startDayDate;
        this.timeH = timeH;
        this.timeM = timeM;
        this.everyH = everyH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getNumberOfTime() {
        return numberOfTime;
    }

    public void setNumberOfTime(String numberOfTime) {
        this.numberOfTime = numberOfTime;
    }

    public String getDoseAmountNumber() {
        return doseAmountNumber;
    }

    public void setDoseAmountNumber(String doseAmountNumber) {
        this.doseAmountNumber = doseAmountNumber;
    }

    public String getDoseAmountText() {
        return doseAmountText;
    }

    public void setDoseAmountText(String doseAmountText) {
        this.doseAmountText = doseAmountText;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTextDurationSpin() {
        return textDurationSpin;
    }

    public void setTextDurationSpin(String textDurationSpin) {
        this.textDurationSpin = textDurationSpin;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getStartDayDate() {
        return startDayDate;
    }

    public void setStartDayDate(String startDayDate) {
        this.startDayDate = startDayDate;
    }

    public String getTimeH() {
        return timeH;
    }

    public void setTimeH(String timeH) {
        this.timeH = timeH;
    }

    public String getTimeM() {
        return timeM;
    }

    public void setTimeM(String timeM) {
        this.timeM = timeM;
    }

    public String getEveryH() {
        return everyH;
    }

    public void setEveryH(String everyH) {
        this.everyH = everyH;
    }
}
