package com.example.safemedicare;

public class GridItem {
    String eventListName;
    String eventListDate;
    String eventListTime;

    //int eventListImage;


    public GridItem(String eventListName, String eventListDate, String eventListTime) {
        this.eventListName = eventListName;
        this.eventListDate = eventListDate;
        this.eventListTime = eventListTime;
    }

    public GridItem(String eventListName, int eventListImage) {
        this.eventListName = eventListName;

    }

    public String getEventListDate() {
        return eventListDate;
    }

    public void setEventListDate(String eventListDate) {
        this.eventListDate = eventListDate;
    }

    public String getEventListTime() {
        return eventListTime;
    }

    public void setEventListTime(String eventListTime) {
        this.eventListTime = eventListTime;
    }

    public String getEventListName() {
        return eventListName;
    }

    public void setEventListName(String eventListName) {
        this.eventListName = eventListName;
    }


}
