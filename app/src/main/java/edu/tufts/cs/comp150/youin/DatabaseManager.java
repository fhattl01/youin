package edu.tufts.cs.comp150.youin;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                profileView.setupProfileView(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkForProfileCreation(final AuthUIActivity auth) {
        DatabaseReference usersRef = ref.child("users").child(uid);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                auth.handleProfileCreation(profile == null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (final DataSnapshot event : dataSnapshot.getChildren()) {
                    final String eventId = event.getValue(String.class);
                    DatabaseReference eventRef = ref.child("events").child(eventId);
                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                            //Remove the event in the list that corresponds to the updated event.
                            for (int i = 0; i < eventList.size(); i++) {
                                Event toRemove = eventList.get(i);
                                if (toRemove.getEventId().equals(eventId)) {
                                    eventList.remove(toRemove);
                                }
                            }
                            eventList.add(dataSnapshot.getValue(Event.class));
                            eventListView.eventViewDataChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getFriendData(final List<Friend> friendList, final FriendListView friendListView) {
        DatabaseReference user = ref.child("users").child(uid).child("friends");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    DatabaseReference friendRef = ref.child("users").child(friend.getValue(String.class));
                    friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = dataSnapshot.getKey();
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            friendList.add(new Friend(profile.getFirstName() + " " + profile.getLastName(), userId, profile.getUsername(), true));
                            friendListView.friendDataChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                friendListView.friendDataChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void searchFriends(final String query, final List<Friend> friendList, final FriendListView friendListView) {
        Query friends = ref.child("users");
        friends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    String friendId = user.getKey();
                    Profile profile = user.getValue(Profile.class);
                    //Safegaurd to ensure username is not null
                    if (profile.getUsername() == null) {
                        profile.setUsername("");
                    }
                    if (profile.getUsername().equals(query) && !uid.equals(friendId)) {
                        boolean alreadyFriends = false;
                        if (profile.getFriends() != null) {
                            alreadyFriends = profile.getFriends().contains(uid);
                        }
                        friendList.add(new Friend(profile.getFirstName() + " " + profile.getLastName(),
                                                  friendId, profile.getUsername(), alreadyFriends));
                    }
                }
                friendListView.friendDataChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFriend(final String friendId) {
        final DatabaseReference myProfileRef = ref.child("users").child(uid).child("friends");
        final DatabaseReference friendProfileRef = ref.child("users").child(friendId).child("friends");
        final GenericTypeIndicator<List<String>> stringList = new GenericTypeIndicator<List<String>>() {};

        myProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                List<String> friends = dataSnapshot.getValue(stringList);
                if (friends == null) {
                    friends = new ArrayList<String>();
                }
                if (!friends.contains(friendId)) {
                    friends.add(friendId);
                    myProfileRef.setValue(friends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        friendProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                List<String> friends = dataSnapshot.getValue(stringList);
                if (friends == null) {
                    friends = new ArrayList<String>();
                }

                if(!friends.contains(uid)) {
                    friends.add(uid);
                    friendProfileRef.setValue(friends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createEvent(Event e) {
        DatabaseReference eventsRef = ref.child("events");

        DatabaseReference pushedEventRef = eventsRef.push();
        final String eventId = pushedEventRef.getKey();
        e.setEventId(eventId);
        pushedEventRef.setValue(e);


        List<String> invited = e.getFriendsInvitedIds();
        invited.add(e.getOwnerId());
        final GenericTypeIndicator<List<String>> stringList = new GenericTypeIndicator<List<String>>() {};

        for (String user : invited) {
            final DatabaseReference userEventsListRef = ref.child("users").child(user).child("events");
            userEventsListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> events = dataSnapshot.getValue(stringList);
                    if (events == null) {
                        events = new ArrayList<String>();
                    }
                    events.add(eventId);
                    userEventsListRef.setValue(events);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void modifyEvent(Event e) {
        String eventId = e.getEventId();
        DatabaseReference eventRef = ref.child("events").child(eventId);
        eventRef.setValue(e);
    }

    public void getEventFriendData(List<String> friendIdList, final List<Friend> friendList, final FriendListView listView) {
        if (friendIdList != null) {
            for (String friendId : friendIdList) {
                DatabaseReference friendRef = ref.child("users").child(friendId);
                friendRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                        String id = dataSnapshot.getKey();
                        Profile friendProfile = dataSnapshot.getValue(Profile.class);
                        Friend f = new Friend(friendProfile.getFirstName() + " " + friendProfile.getLastName(),
                                id, friendProfile.getUsername(), true);
                        friendList.add(f);
                        listView.friendDataChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
