package edu.tufts.cs.comp150.youin;


import android.widget.CheckBox;

public class Friend{
    private String friendName;
    private String friendId;
    private boolean isFriend;

    public Friend(String friendName, String friendId, boolean isCurrentFriend) {//, Date dateOfEvent, Time timeofEvent) {
        this.friendName = friendName;
        this.friendId = friendId;
        isFriend = isCurrentFriend;
    }

    public String getName()
    {
        return friendName;
    }

    public String getId() {
        return friendId;
    }

    public boolean isFriend() {
        return isFriend;
    }
}