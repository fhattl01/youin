package edu.tufts.cs.comp150.youin;


import android.widget.CheckBox;

public class Friend{
    private String friendName;
    private String friendId;
    private boolean invited;

    public Friend(String friendName, String friendId) {//, boolean invited) {, Date dateOfEvent, Time timeofEvent) {
        this.friendName = friendName;
        this.friendId = friendId;
        this.invited = false;
    }

    public Friend(String friendName, String friendId, boolean invited) {
        this.friendName = friendName;
        this.friendId = friendId;
        this.invited = invited;
    }

    public String getName()
    {
        return friendName;
    }

    public String getId() {
        return friendId;
    }

    public boolean getInvited() {
        return invited;
    }

    public void flipInvite() {
        invited = !invited;
    }


}
