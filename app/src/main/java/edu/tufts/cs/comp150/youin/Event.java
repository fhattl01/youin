package edu.tufts.cs.comp150.youin;

import android.widget.TextView;

import java.sql.Time;
import java.util.Date;

/**
 * Created by franceshughes on 10/14/17.
 */

public class Event {
    private String eventId;
    private String name;
    private String description;
    private Date dateOfEvent;
    private Time timeOfEvent;

    public Event() {
    }

    public Event(String eventId, String name, String description) {//, Date dateOfEvent, Time timeofEvent) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
     //   this.dateOfEvent = dateOfEvent;
     //   this.timeOfEvent = timeofEvent;
    }


    public String getName() {
        return name;
    }

    public String getEventId() {
        return eventId;
    }

    public String getDescription() {
        return description;
    }
/*
    public Date getDateofEvent() {
        return dateOfEvent;
    }

    public Time getTimeOfEvent() {
        return timeOfEvent;
    }*/
}
