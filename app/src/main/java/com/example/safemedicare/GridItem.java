package com.example.safemedicare;

public class GridItem {
    String eventListName;
    int eventListImage;

    public GridItem(String eventListName, int eventListImage) {
        this.eventListName = eventListName;
        this.eventListImage = eventListImage;
    }

    public String getEventListName() {
        return eventListName;
    }

    public void setEventListName(String eventListName) {
        this.eventListName = eventListName;
    }

    public int getEventListImage() {
        return eventListImage;
    }

    public void setEventListImage(int eventListImage) {
        this.eventListImage = eventListImage;
    }
}
