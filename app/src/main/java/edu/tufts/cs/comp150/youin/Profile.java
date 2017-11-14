package edu.tufts.cs.comp150.youin;

import android.media.Image;

import com.firebase.ui.auth.User;

import java.util.List;

/**
 * Created by Frank on 10/16/17.
 */

public class Profile {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageReference;

    private List<String> friends;
    private List<String> groups;
    private List<String> events;

    public Profile() {

    }

    public Profile(String firstName, String lastName, String username, String emailAddress, String imageReference,
                   List<String> friendIds, List<String> groupIds, List<String> eventIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        email = emailAddress;
        this.imageReference = imageReference;
        friends = friendIds;
        groups = groupIds;
        events = eventIds;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImageReference() {
        return imageReference;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getEvents() {
        return events;
    }
}
