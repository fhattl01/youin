package edu.tufts.cs.comp150.youin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class EventListActivity extends AppCompatActivity implements EventListView {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter eAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        eAdapter = new EventRecyclerViewAdapter(eventList, getApplicationContext());
        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(eLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eAdapter);

        prepareEventData();
    }

    private void prepareEventData() {
        DatabaseManager profileManager = new DatabaseManager(firebaseUser.getUid());
        profileManager.getEventData(eventList, this);
    }

    public void createEventButton(View v) {
        startActivity(new Intent(this, CreateEventActivity.class));
    }

    public void viewProfile(View v) {
        startActivity(new Intent(this, ProfileViewActivity.class));
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

    @Override
    public void eventViewDataChanged() {
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return (int)(t1.getStartTime() - event.getStartTime());
            }
        });
        eAdapter.notifyDataSetChanged();
    }

    public void viewFriends(View v) {
        startActivity(new Intent(this, ManageFriendsActivity.class));
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

}
