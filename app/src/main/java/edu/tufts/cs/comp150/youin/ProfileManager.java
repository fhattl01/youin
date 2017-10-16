package edu.tufts.cs.comp150.youin;

import android.media.Image;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Frank on 10/12/17.
 */

public class ProfileManager {

    private String firstName;
    private String lastName;
    private String email;
    private String uid;
    private Image profilePicture;

    private String[] friendsIds;
    private String[] groupIds;
    private DatabaseReference ref;


    public ProfileManager(String userId) {
        ref = FirebaseDatabase.getInstance().getReference();
        uid = userId;
    }

    public void getProfileViewData(final ProfileView profileView) {
        DatabaseReference user = ref.child("users").child(uid);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firstName = (String)dataSnapshot.child("fn").getValue();
                lastName = (String)dataSnapshot.child("ln").getValue();
                email = (String)dataSnapshot.child("email").getValue();
                profilePicture = null;

                profileView.setupProfileView(firstName, lastName, email, profilePicture);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "profile view data cancelled");
            }
        });
    }

    public void getEventData(final List<Event> eventList, final EventListView eventListView) {
        DatabaseReference user = ref.child("users").child(uid).child("events");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    String key = event.getKey();
                    eventList.add(new Event(key, "event" + key, "test description"));
                }
                eventListView.eventViewDataChanged();
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
                    DatabaseReference friendRef = ref.child("users").child(friend.getKey());
                    friendRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = dataSnapshot.getKey();
                            String fn = (String)dataSnapshot.child("fn").getValue();
                            String ln = (String)dataSnapshot.child("ln").getValue();
                            friendList.add(new Friend(fn + " " + ln, userId));
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
}
