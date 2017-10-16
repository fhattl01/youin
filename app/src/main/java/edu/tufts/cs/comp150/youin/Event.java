package edu.tufts.cs.comp150.youin;

import android.widget.TextView;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by franceshughes on 10/14/17.
 */

public class Event {
    private String eventId;
    private String name;
    private String description;
    private String location;
    private Date dateOfEvent;
    private Time start;
    private Time rsvpDeadline;
    private List<String> friendsGoingIds;
    private List<String> friendsDeclinedIds;
    private int minPeopleToGo;
    private String owner;



    public Event() {
    }

    public Event(String eventId, String name, String description) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
     //   this.dateOfEvent = dateOfEvent;
     //   this.timeOfEvent = timeofEvent;
    }

    public Event(String id, String eventName, String eventDescription, String eventLocation,
                 Date date, Time startTime, Time respondDeadline, List<String> going,
                 List<String> declined, int minimumPeople, String ownerId) {
        eventId = id;
        name = eventName;
        description = eventDescription;
        location = eventLocation;
        dateOfEvent = date;
        start = startTime;
        rsvpDeadline = respondDeadline;
        friendsGoingIds = going;
        friendsDeclinedIds = declined;
        minPeopleToGo = minimumPeople;
        owner = ownerId;

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
