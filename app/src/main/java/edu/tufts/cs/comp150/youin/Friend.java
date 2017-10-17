package edu.tufts.cs.comp150.youin;


import android.widget.CheckBox;

public class Friend{
    private String friendName;
    private String friendId;

    public Friend(String friendName, String friendId) {//, Date dateOfEvent, Time timeofEvent) {
        this.friendName = friendName;
        this.friendId = friendId;
    }

    public String getName()
    {
        return friendName;
    }

    public String getId() {
        return friendId;
    }



}