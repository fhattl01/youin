package edu.tufts.cs.comp150.youin;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CreateProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void createProfile(View v){
        EditText firstName = (EditText)findViewById(R.id.enterFirstName);
        EditText lastName = (EditText)findViewById(R.id.enterLastName);
        EditText username = (EditText)findViewById(R.id.enterUsername);
        List<String> friends = new ArrayList<>();
        List<String> dummyList = new ArrayList<>();

        if (firstName.getText().toString().equals("")) {
            showToast("Please enter a first name");
        } else if (lastName.getText().toString().equals("")) {
            showToast("Please enter a last name");
        } else if (username.getText().toString().equals("")) {
            showToast("Please enter a username");
        } else {
            DatabaseManager manger = new DatabaseManager(firebaseUser.getUid());
            Profile profile = new Profile(firstName.getText().toString(), lastName.getText().toString(),
                    username.getText().toString(), firebaseUser.getEmail(),
                    "", friends, dummyList, dummyList);
            manger.createProfile(profile);
            startActivity(new Intent(this, EventListActivity.class));
        }

    }

    private void showToast(String message) {
        CoordinatorLayout layout = findViewById(R.id.coordinatorLayout);
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
    }
}
