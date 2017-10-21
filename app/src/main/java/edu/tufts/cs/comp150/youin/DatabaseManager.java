package edu.tufts.cs.comp150.youin;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

/**
 * Created by Frank on 10/12/17.
 */

public class DatabaseManager {

    private String uid;
    private DatabaseReference ref;


    public DatabaseManager(String userId) {
        ref = FirebaseDatabase.getInstance().getReference();
        uid = userId;
    }

    public void getProfileViewData(final ProfileView profileView) {
        DatabaseReference user = ref.child("users").child(uid);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                profileView.setupProfileView(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "profile view data cancelled");
            }
        });
    }

    public void checkForProfileCreation(final AuthUIActivity auth) {
        DatabaseReference usersRef = ref.child("users").child(uid);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                auth.handleProfileCreation(profile == null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "profile view data cancelled");
            }
        });
    }

    public void createProfile(Profile profile) {
        DatabaseReference profileRef = ref.child("users");
        profileRef.child(uid).setValue(profile);
    }

    public void getEventData(final List<Event> eventList, final EventListView eventListView) {
        DatabaseReference user = ref.child("users").child(uid).child("events");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (final DataSnapshot event : dataSnapshot.getChildren()) {
                    String eventId = event.getKey();
                    DatabaseReference eventRef = ref.child("events").child(eventId);
                    eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            eventList.add(dataSnapshot.getValue(Event.class));
                            eventListView.eventViewDataChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Database", "profile view data cancelled");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "profile view data cancelled");
            }
        });
    }

    public void getFriendData(final List<Friend> friendList, final FriendListView friendListView) {
        DatabaseReference user = ref.child("users").child(uid).child("friends");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    DatabaseReference friendRef = ref.child("users").child(friend.getValue(String.class));
                    friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = dataSnapshot.getKey();
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            friendList.add(new Friend(profile.getFirstName() + " " + profile.getLastName(), userId));
                            friendListView.friendDataChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Database", "profile view data cancelled");
                        }
                    });

                }
                friendListView.friendDataChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "profile view data cancelled");
            }
        });
    }

    public void searchFriends(String query, final FriendListView friendListView) {
        friendListView.friendDataChanged();
    }

    public void createEvent(Event e) {
        DatabaseReference eventsRef = ref.child("events");

        DatabaseReference pushedEventRef = eventsRef.push();
        pushedEventRef.setValue(e);
        String eventId = pushedEventRef.getKey();

        List<String> invited = e.getFriendsInvitedIds();
        invited.add(e.getOwner());

        for (String user : invited) {
            DatabaseReference userEventsListRef = ref.child("users").child(user).child("events");
            userEventsListRef.child(eventId).setValue(true);
        }
    }
}
