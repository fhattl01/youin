package edu.tufts.cs.comp150.youin;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateEventActivity extends AppCompatActivity implements FriendListView {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private InviteListAdapter inviteListAdapter;
    private List<Friend> friends;
    private ListView friendsList;
    private DatabaseManager manager;
    private List<String> invitedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        invitedList = new ArrayList<String>();

        //Now check if this user is null
        if (firebaseUser == null){
            Intent login = new Intent(this, AuthUIActivity.class);
            startActivity(login);
        }

        CardView what = (CardView)findViewById(R.id.whatView);
        what.setVisibility(View.VISIBLE);
        Button whatButton = (Button)findViewById(R.id.createEventWhatButton);
        whatButton.setPaintFlags(whatButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        friends = new ArrayList<>(25);
        inviteListAdapter = new InviteListAdapter(friends, getApplicationContext());
        friendsList = (ListView)findViewById(R.id.inviteList);
        friendsList.setAdapter(inviteListAdapter);

        manager = new DatabaseManager(firebaseUser.getUid());
        manager.getFriendData(friends, this);
        displayFriendList();
    }

    private void displayFriendList() {
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("INVITE", "In onclicked");
                Friend friend = friends.get(i);
                friend.flipInvite();
                //friends.set(i, friend);
                inviteListAdapter.notifyDataSetChanged();
                if (friend.getInvited()) {
                    invitedList.add(friend.getId().toString());
                } else {
                    invitedList.remove(friend.getId().toString());
                }
            }
        });
    }

    public void navigateToWhat(View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHAT);
    }

    public void navigateToWhen(View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHEN);
    }

    public void navigateToWho(View v) {

        handleEventAttributeCategoryChange(EventAttributeCategory.WHO);
        //test populate the list view


        Spinner min_people = (Spinner) findViewById(R.id.minPeopleSpinner);

        ArrayAdapter<CharSequence> people = ArrayAdapter.createFromResource(
                this,
                R.array.min_people_amount,
                android.R.layout.simple_spinner_item);

        people.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        min_people.setAdapter(people);
    }

    public void navigateToWhere (View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHERE);
    }


    private void handleEventAttributeCategoryChange(EventAttributeCategory changeTo) {
        Button what = (Button)findViewById(R.id.createEventWhatButton);
        Button when = (Button)findViewById(R.id.createEventWhenButton);
        Button who = (Button)findViewById(R.id.createEventWhoButton);
        Button where = (Button)findViewById(R.id.createEventWhereButton);

        what.setPaintFlags(what.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        when.setPaintFlags(when.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        who.setPaintFlags(who.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        where.setPaintFlags(where.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

        CardView whatCard = (CardView)findViewById(R.id.whatView);
        CardView whenCard = (CardView)findViewById(R.id.whenView);
        CardView whoCard = (CardView)findViewById(R.id.whoView);
        CardView whereCard = (CardView)findViewById(R.id.whereView);
        whatCard.setVisibility(View.GONE);
        whenCard.setVisibility(View.GONE);
        whoCard.setVisibility(View.GONE);
        whereCard.setVisibility(View.GONE);

        Button backButton = (Button)findViewById(R.id.backButton);
        Button nextButton = (Button)findViewById(R.id.nextButton);
        Button sendInvite = (Button)findViewById(R.id.sendInvite);

        switch (changeTo) {
            case WHAT:
                what.setPaintFlags(what.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                whatCard.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.GONE);
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhen(v);
                    }
                });
                break;
            case WHEN:
                when.setPaintFlags(when.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                whenCard.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhat(v);
                    }
                });
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWho(v);
                    }
                });
                break;
            case WHO:
                who.setPaintFlags(who.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                whoCard.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhen(v);
                    }
                });
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhere(v);
                    }
                });
                break;
            case WHERE:
                where.setPaintFlags(where.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                whereCard.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWho(v);
                    }
                });
                sendInvite.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);

                break;
        }
    }

    public void toggleWhenSelector(View v) {
        Button toggle = (Button)findViewById(R.id.whenSelectorToggle);
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

        if (timePicker.getVisibility() == View.GONE) {
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
            toggle.setText(R.string.toggleToDatePicker);
        } else {
            datePicker.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.GONE);
            toggle.setText(R.string.toggleToTimePicker);
        }
    }

    public void sendInvite(View view) {
        manager = new DatabaseManager(firebaseUser.getUid());
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        TimePicker eventStartTime = (TimePicker) findViewById(R.id.timePicker);
        String eventOwnerId = (String) firebaseUser.getUid();

        Event e = new Event(eventName.getText().toString(), eventDescription.getText().toString(),
                            eventLocation.getText().toString(), null, null, invitedList, null,
                            null, 0, eventOwnerId);
        manager.createEvent(e);

       // TextView feedback = (TextView) findViewById(R.id.feedback);
       // feedback.setText("your Invitation has been sent!");
    }

    public void cancelEventCreation(View v) {

        startActivity(new Intent(this, EventListActivity.class));
    }

    @Override
    public void friendDataChanged() {
        inviteListAdapter.notifyDataSetChanged();
    }

    public void inviteFriendsButton(View v) {
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.friendCheck);
        TextView id = (TextView) v.findViewById(R.id.friendId);
        if (checkBox.isChecked()) {
            invitedList.add(id.getText().toString());
            Log.d("INVITE", invitedList.toString());
        } else {
            invitedList.remove(id.getText().toString());
            Log.d("INVITE", invitedList.toString());

        }
    }

    public void createEvent() {

    }
}
