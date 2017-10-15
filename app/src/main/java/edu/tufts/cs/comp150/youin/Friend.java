package edu.tufts.cs.comp150.youin;


public class Friend{
    private String friendId;
    private String friendName;

    public Friend(){
    }

    public Friend(String friendId, String friendName) {//, Date dateOfEvent, Time timeofEvent) {
        this.friendId = friendId;
        this.friendName = friendName;
    }


    public String getName()
    {
        return friendName;
    }

    public String getFriendId() {
        return friendId;
    }

}