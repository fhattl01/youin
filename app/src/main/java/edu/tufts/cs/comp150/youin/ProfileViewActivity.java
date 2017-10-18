package edu.tufts.cs.comp150.youin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewActivity extends AppCompatActivity implements ProfileView {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Now check if this user is null
        if (firebaseUser == null) {
            Intent login = new Intent(this, AuthUIActivity.class);
            startActivity(login);
        } else {
            name = (TextView)findViewById(R.id.profileName);
            email = (TextView)findViewById(R.id.profileEmail);
            DatabaseManager profile = new DatabaseManager(firebaseUser.getUid());
            profile.getProfileViewData(this);
        }
    }

    public void setViewParameters(DatabaseManager profile) {
        TextView view = (TextView)findViewById(R.id.profileName);
    }

    @Override
    public void setupProfileView(Profile profile) {
        this.name.setText(profile.getFirstName() + " " + profile.getLastName());
        this.email.setText(profile.getEmail());
    }
}
