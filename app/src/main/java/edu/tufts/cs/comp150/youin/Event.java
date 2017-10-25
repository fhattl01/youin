package edu.tufts.cs.comp150.youin;

import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by franceshughes on 10/14/17.
 */

public class Event {
    private String name;
    private String description;
    private String location;
    private Date startTime;
    private Date rsvpDeadline;
    private List<String> friendsInvitedIds;
    private List<String> friendsGoingIds;
    private List<String> friendsDeclinedIds;
    private int minPeopleToGo;
    private String ownerId;
    private String eventId;



    public Event() {
    }

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
     //   this.dateOfEvent = dateOfEvent;
     //   this.timeOfEvent = timeofEvent;
    }

    public Event(String eventName, String eventDescription, String eventLocation, Date eventStartTime, Date respondDeadline, List<String> friendsInvited, List<String> going,
                 List<String> declined, int minimumPeople, String eventOwnerId) {
        name = eventName;
        description = eventDescription;
        location = eventLocation;
        startTime = eventStartTime;
        rsvpDeadline = respondDeadline;
        friendsInvitedIds = friendsInvited;
        friendsGoingIds = going;
        friendsDeclinedIds = declined;
        minPeopleToGo = minimumPeople;
        ownerId = eventOwnerId;
        eventId = null;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getRsvpDeadline() {
        return rsvpDeadline;
    }

    public List<String> getFriendsInvitedIds() {
        return friendsInvitedIds;
    }

    public List<String> getFriendsGoingIds() {
        return friendsGoingIds;
    }

    public List<String> getFriendsDeclinedIds() {
        return friendsDeclinedIds;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getMinPeopleToGo() {
        return minPeopleToGo;
    }

    public void respondGoing(String uid) {
        if (friendsInvitedIds != null) {
            friendsInvitedIds.remove(uid);
        }
        if (friendsGoingIds != null) {
            if (!friendsGoingIds.contains(uid)) {
                friendsGoingIds.add(uid);
            }
        } else {
            friendsGoingIds = new ArrayList<>();
            friendsGoingIds.add(uid);
        }
        if (friendsDeclinedIds != null) {
            friendsDeclinedIds.remove(uid);
        }

    }

    public void respondDeclined(String uid) {
        if (friendsInvitedIds != null) {
            friendsInvitedIds.remove(uid);
        }
        if (friendsDeclinedIds != null) {
            if (!friendsDeclinedIds.contains(uid)) {
                friendsDeclinedIds.add(uid);
            }
        } else {
            friendsDeclinedIds = new ArrayList<>();
            friendsDeclinedIds.add(uid);
        }
        if (friendsGoingIds != null) {
            friendsGoingIds.remove(uid);
        }

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        eventId = id;
    }

    public boolean isGoing(String userId) {
        if (friendsGoingIds == null) {
            return false;
        } else {
            return friendsGoingIds.contains(userId);
        }
    }

    public boolean isInvited(String userId) {
        if (friendsInvitedIds == null) {
            return false;
        } else {
            return friendsInvitedIds.contains(userId);
        }
    }
}
