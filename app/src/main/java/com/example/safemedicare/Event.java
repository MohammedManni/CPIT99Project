package com.example.safemedicare;

public class Event {
    String EventName, EventDetails ,EventDate,EventTimeH,EventTimeM;

    public Event() {

    }

    public Event(String eventName, String eventDetails, String eventTimeH, String eventTimeM, String eventDate) {
        EventName = eventName;
        EventDetails = eventDetails;
        EventTimeH = eventTimeH;
        EventTimeM = eventTimeM;
        EventDate = eventDate;
    }


    public String getEventTimeH() {
        return EventTimeH;
    }

    public void setEventTimeH(String eventTimeH) {
        EventTimeH = eventTimeH;
    }

    public String getEventTimeM() {
        return EventTimeM;
    }

    public void setEventTimeM(String eventTimeM) {
        EventTimeM = eventTimeM;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDetails() {
        return EventDetails;
    }

    public void setEventDetails(String eventDetails) {
        EventDetails = eventDetails;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }


}
