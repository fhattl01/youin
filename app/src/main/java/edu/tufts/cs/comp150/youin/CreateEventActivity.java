package edu.tufts.cs.comp150.youin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.Spinner;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;


public class CreateEventActivity extends AppCompatActivity implements FriendListView {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private InviteListAdapter inviteListAdapter;
    private List<Friend> friends;
    private ListView friendsList;
    private DatabaseManager manager;
    private List<String> invitedList;
    private int mYear, mMonth, mDay, hour, eventMinute;
    private Calendar eventStartTime;
    private Calendar rsvpDeadline;
    private String am_pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        invitedList = new ArrayList<String>();

        eventStartTime = Calendar.getInstance();
        mYear = eventStartTime.get(Calendar.YEAR);
        mMonth = eventStartTime.get(Calendar.MONTH);
        mDay = eventStartTime.get(Calendar.DAY_OF_MONTH);
        hour = eventStartTime.get(Calendar.HOUR_OF_DAY);
        eventMinute = eventStartTime.get(Calendar.MINUTE);
        am_pm = "";
        rsvpDeadline = null;

        //Now check if this user is null
        if (firebaseUser == null){
            Intent login = new Intent(this, AuthUIActivity.class);
            startActivity(login);
        }

        CardView eventInformation = (CardView)findViewById(R.id.eventInformationCard);
        eventInformation.setVisibility(View.VISIBLE);
        Button infoButton = (Button)findViewById(R.id.createEventInformationButton);
        infoButton.setPaintFlags(infoButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        friends = new ArrayList<>(25);
        inviteListAdapter = new InviteListAdapter(friends, getApplicationContext());
        friendsList = (ListView)findViewById(R.id.inviteList);
        friendsList.setAdapter(inviteListAdapter);

        manager = new DatabaseManager(firebaseUser.getUid());
        manager.getFriendData(friends, this);
        displayFriendList();
        invitedList.add(firebaseUser.getUid());

        final Button pickTime = (Button) findViewById(R.id.pickTime);
        final TextView timeTextView = (TextView) findViewById(R.id.time);
        final Button pickDate = (Button) findViewById(R.id.pickDate);
        final TextView dateTextView = (TextView) findViewById(R.id.date);

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                SimpleDateFormat stz = new SimpleDateFormat("h:mm a", Locale.US);
                                hour = hourOfDay;
                                eventMinute = minute;
                                eventStartTime.set(mYear, mMonth, mDay, hour, eventMinute);
                                pickTime.setText(stz.format(eventStartTime.getTime()));
                            }
                        }, hour, eventMinute, false);
                timePickerDialog.show();
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int currentYear = c.get(Calendar.YEAR);
                final int currentMonth = c.get(Calendar.MONTH);
                final int currentDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (year < currentYear)
                                    view.updateDate(currentYear, currentMonth, currentDay);
                                if (monthOfYear < currentMonth && year == currentYear)
                                    view.updateDate(currentYear,currentMonth,currentDay);

                                if (dayOfMonth < currentDay && year == currentYear && monthOfYear == currentMonth)
                                    view.updateDate(currentYear,currentMonth,currentDay);
                                String dateText = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                                pickDate.setText(dateText);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                eventStartTime.set(mYear, mMonth, mDay, hour, eventMinute);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

    }

    private void displayFriendList() {
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Friend friend = friends.get(i);
                friend.flipInvite();
                inviteListAdapter.notifyDataSetChanged();
                if (friend.getInvited()) {
                    invitedList.add(friend.getId().toString());
                } else {
                    invitedList.remove(friend.getId().toString());
                }
            }
        });
    }

    public void navigateToEventInformation(View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.INFORMATION);
        TextView eventName = (TextView)findViewById(R.id.eventName);
        eventName.requestFocus();
    }

    public void navigateToAddFriends(View v) {

        handleEventAttributeCategoryChange(EventAttributeCategory.ADD_FRIENDS);
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.topLevelEventCreation);
        myLayout.requestFocus();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void handleEventAttributeCategoryChange(EventAttributeCategory changeTo) {
        Button informationButton = (Button)findViewById(R.id.createEventInformationButton);
        Button addFriendsButton = (Button)findViewById(R.id.createEventAddFriendsButton);

        informationButton.setPaintFlags(informationButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        addFriendsButton.setPaintFlags(addFriendsButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

        CardView infoCard = (CardView)findViewById(R.id.eventInformationCard);
        CardView addFriendsCard = (CardView)findViewById(R.id.addFriendsCard);
        infoCard.setVisibility(View.GONE);
        addFriendsCard.setVisibility(View.GONE);

        Button backButton = (Button)findViewById(R.id.backButton);
        Button nextButton = (Button)findViewById(R.id.nextButton);
        Button sendInvite = (Button)findViewById(R.id.sendInvite);

        switch (changeTo) {
            case INFORMATION:
                informationButton.setPaintFlags(informationButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                infoCard.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.GONE);
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToAddFriends(v);
                    }
                });
                break;
            case ADD_FRIENDS:
                addFriendsButton.setPaintFlags(addFriendsButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                addFriendsCard.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToEventInformation(v);
                    }
                });
                sendInvite.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                break;
        }
    }

    public void sendInvite(View view) {
        manager = new DatabaseManager(firebaseUser.getUid());

        TextView eventName = (TextView) findViewById(R.id.eventName);
        String name = eventName.getText().toString();

        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        String description = eventDescription.getText().toString();

        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        String location = eventLocation.getText().toString();

        long startTime = eventStartTime.getTimeInMillis();

        TextView minPeopleView = (TextView) findViewById(R.id.minPeopleNumber);
        int minPeople = Integer.parseInt(minPeopleView.getText().toString());


        String eventOwnerId = (String) firebaseUser.getUid();

        if (name.equals("") || name == null) {
            showToast(R.string.noEventName);
        } else if (description == null) {
            description = "";
        } else if (location == null) {
            location = "";
        } else if (startTime < Calendar.getInstance().getTimeInMillis()) {
            showToast(R.string.invalidEventTime);
        } else {

            Event e = new Event(name, description, location, startTime, 0, invitedList,
                    null, null, minPeople, eventOwnerId);
            manager.createEvent(e);
            startActivity(new Intent(this, EventListActivity.class));
        }
    }

    public void cancelEventCreation(View v) {
        startActivity(new Intent(this, EventListActivity.class));
    }

    public void decrementMinPeople(View v) {
        TextView minPeopleView = (TextView) findViewById(R.id.minPeopleNumber);
        int minPeople = Integer.parseInt(minPeopleView.getText().toString());
        if (minPeople > 0) {
            minPeople--;
            String format = String.format("%d", minPeople);
            minPeopleView.setText(format);
        }
    }

    public void incrementMinPeople(View v) {
        TextView minPeopleView = (TextView) findViewById(R.id.minPeopleNumber);
        int minPeople = Integer.parseInt(minPeopleView.getText().toString());
        minPeople++;
        String format = String.format("%d", minPeople);
        minPeopleView.setText(format);
    }

    @Override
    public void friendDataChanged() {
        inviteListAdapter.notifyDataSetChanged();
    }

    private void showToast(@StringRes int errorMessageRes) {
        View rootView = findViewById(R.id.createEventRoot);
        Snackbar.make(rootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    public void toEventList(View v) {
        startActivity(new Intent(this, EventListActivity.class));
    }
}
