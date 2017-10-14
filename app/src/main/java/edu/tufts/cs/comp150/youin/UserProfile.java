package edu.tufts.cs.comp150.youin;

import android.media.Image;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by maggi on 10/12/17.
 */

public class UserProfile {

    private String firstName;
    private String lastName;
    private String email;
    private String uid;
    private Image profilePicture;

    private String[] friendsIds;
    private String[] groupIds;
    private String[] eventIds;
    DatabaseReference ref;


    public UserProfile (String userId) {
        uid = userId;
        ref = FirebaseDatabase.getInstance().getReference();
        //ref.child("users").child(uid);

    }
}
