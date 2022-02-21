package com.example.safemedicare;

import java.sql.Array;
import java.util.ArrayList;

public class SharedCalendar {
    ArrayList<Event> EventList = new ArrayList<Event>();

    public void AddEvent(Event event) {
        EventList.add(event);

    }

    public void DeleteEvent(Event event) {
        EventList.remove(event);
    }

    public Event DisplayEventDetails() {
        if (EventList.size() != 0)
            for (int i = 0; i < EventList.size(); i++) {
                return EventList.get(i);
            }
        return null;
    }
}
