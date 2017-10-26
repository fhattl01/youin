package edu.tufts.cs.comp150.youin;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventHolder> {

    private List<Event> eventList;
    private FirebaseUser user;
    private String userId;
    private DatabaseManager manager;
    private Context applicationContext;


    public class EventHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public Button attending, notAttending;
        public ListView friendsAttending, friendsNotAttending, friendsInvited;
        public TextView timeOfEvent;
        public TextView dateOfEvent;
        public TextView minPeople;


        public EventHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.eventListName);
            description = (TextView) view.findViewById(R.id.eventListDescription);
            attending = (Button) view.findViewById(R.id.attendingButton);
            notAttending = (Button) view.findViewById(R.id.notAttendingButton);
            user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();
            manager = new DatabaseManager(userId);
            friendsAttending = (ListView) view.findViewById(R.id.friendsAttending);
            friendsNotAttending = (ListView) view.findViewById(R.id.friendsNotAttending);
            friendsInvited = (ListView) view.findViewById(R.id.friendsInvited);
            timeOfEvent = (TextView) view.findViewById(R.id.timeOfEvent);
            dateOfEvent = (TextView) view.findViewById(R.id.dateOfEvent);
            minPeople = (TextView) view.findViewById(R.id.minPeople);
        }
    }


    public EventRecyclerViewAdapter(List<Event> eventList, Context applicationContext) {
        this.eventList = eventList;
        this.applicationContext = applicationContext;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, int position) {
        final Event event = eventList.get(position);
        Log.d("ATTENDING", "Event Name");

        if (event != null) {
            int backgroundColor = Color.parseColor("#3a65c9");
            int noDecisionColor = Color.parseColor("#FFFFFF");
            holder.name.setText(event.getName());
            holder.description.setText(event.getDescription());
            String minPeopleText = "This event has enough people!";
            holder.minPeople.setTextColor(ContextCompat.getColor(applicationContext, R.color.green));
            int minPeople = event.getMinPeopleToGo();
            int peopleGoing = 0;
            if (event.getFriendsGoingIds() != null) {
                peopleGoing = event.getFriendsGoingIds().size();
            }
            if (minPeople > peopleGoing) {
                int peopleNeeded = minPeople - peopleGoing;
                if (peopleNeeded == 1) {
                    minPeopleText = "This event needs 1 more person";
                    holder.minPeople.setTextColor(ContextCompat.getColor(applicationContext, R.color.red));
                } else {
                    minPeopleText = "This event needs " + Integer.toString(peopleNeeded) + " more people";
                    holder.minPeople.setTextColor(ContextCompat.getColor(applicationContext, R.color.red));
                }
            }
            holder.minPeople.setText(minPeopleText);
            Calendar c = Calendar.getInstance();

            c.setTimeInMillis(event.getStartTime());
            String dateFormat = "MM/dd/yyyy";
            String timeFormat = "h:mm a";
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
            SimpleDateFormat timeFormatter = new SimpleDateFormat(timeFormat);
            holder.timeOfEvent.setText(dateFormatter.format(c.getTime()));
            holder.dateOfEvent.setText(timeFormatter.format(c.getTime()));

            if (event.isGoing(userId)) {
                holder.attending.setBackgroundColor(backgroundColor);
                holder.notAttending.setBackgroundColor(noDecisionColor);
            } else if (event.isInvited(userId)) {
                holder.attending.setBackgroundColor(noDecisionColor);
                holder.notAttending.setBackgroundColor(noDecisionColor);
            } else {
                holder.notAttending.setBackgroundColor(backgroundColor);
                holder.attending.setBackgroundColor(noDecisionColor);
            }

            holder.attending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.respondGoing(user.getUid());
                    manager.modifyEvent(event);
                }
            });

            holder.notAttending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.respondDeclined(user.getUid());
                    manager.modifyEvent(event);
                }
            });


            List<String> friendsGoingIds = event.getFriendsGoingIds();
            List<Friend> friendsGoing = new ArrayList<>();
            final EventInvitedUserAdapter friendsListAdapterAccepted = new EventInvitedUserAdapter(friendsGoing,
                    applicationContext);
            FriendListView goingView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterAccepted.notifyDataSetChanged();
                    UIUtils.setListViewHeightBasedOnItems(holder.friendsAttending);
                }
            };
            manager.getEventFriendData(friendsGoingIds, friendsGoing, goingView);

            List<String> friendsNotGoingIds = event.getFriendsDeclinedIds();
            List<Friend> friendsNotGoing = new ArrayList<>();
            final EventInvitedUserAdapter friendsListAdapterDeclined = new EventInvitedUserAdapter(friendsNotGoing,
                    applicationContext);
            FriendListView notGoingView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterDeclined.notifyDataSetChanged();
                    UIUtils.setListViewHeightBasedOnItems(holder.friendsNotAttending);
                }
            };
            manager.getEventFriendData(friendsNotGoingIds, friendsNotGoing, notGoingView);

            List<String> friendsInvitedIds = event.getFriendsInvitedIds();
            List<Friend> friendsInvited = new ArrayList<>();
            final EventInvitedUserAdapter friendsListAdapterInvited = new EventInvitedUserAdapter(friendsInvited,
                    applicationContext);
            FriendListView invitedView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterInvited.notifyDataSetChanged();
                    UIUtils.setListViewHeightBasedOnItems(holder.friendsInvited);
                }
            };
            manager.getEventFriendData(friendsInvitedIds, friendsInvited, invitedView);

            holder.friendsAttending.setAdapter(friendsListAdapterAccepted);
            UIUtils.setListViewHeightBasedOnItems(holder.friendsAttending);
            holder.friendsNotAttending.setAdapter(friendsListAdapterDeclined);
            UIUtils.setListViewHeightBasedOnItems(holder.friendsNotAttending);
            holder.friendsInvited.setAdapter(friendsListAdapterInvited);
            UIUtils.setListViewHeightBasedOnItems(holder.friendsInvited);

        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}