package edu.tufts.cs.comp150.youin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EventListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Now check if this user is null
        if (firebaseUser == null) {
            Intent login = new Intent(this, AuthUIActivity.class);
            startActivity(login);
        }

        TextView v = (TextView) findViewById(R.id.displayBox);
        if (firebaseUser != null) {
            v.setText(firebaseUser.getUid());
        }
    }

    public void createEventButton(View v) {
        startActivity(new Intent(this, CreateEventActivity.class));
    }

    public void logOut(View v) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(EventListActivity.this, AuthUIActivity.class));
                            finish();
                        }
                    });
        }
}
