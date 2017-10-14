package edu.tufts.cs.comp150.youin;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String[] eventIds;
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
}
