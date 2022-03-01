package com.example.safemedicare;

import java.util.Date;

public class Event {
    String EventTitle, EventDetails;
    Date EventDate;

    public Event() {

    }

    public Event(String eventTitle, String eventDetails, Date eventDate) {
        EventTitle = eventTitle;
        EventDetails = eventDetails;
        EventDate = eventDate;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventDetails() {
        return EventDetails;
    }

    public void setEventDetails(String eventDetails) {
        EventDetails = eventDetails;
    }

    public Date getEventDate() {
        return EventDate;
    }

    public void setEventDate(Date eventDate) {
        EventDate = eventDate;
    }


}
