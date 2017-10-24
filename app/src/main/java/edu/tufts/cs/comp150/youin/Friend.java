package edu.tufts.cs.comp150.youin;


import android.widget.CheckBox;

public class Friend{
    private String friendName;
    private String friendId;
    private boolean isFriend;
    private boolean invited;

    public Friend(String friendName, String friendId, boolean isCurrentFriend) {
        this.friendName = friendName;
        this.friendId = friendId;
        isFriend = isCurrentFriend;
        invited = false;
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

    public boolean getInvited() {
        return invited;
    }

    public void flipInvite() {
        invited = !invited;
    }
}
