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
        public TextView name, description;// date, time;
        public Button attending, notAttending;
        public ListView friendsAttending, friendsNotAttending, friendsInvited;
        public TextView timeOfEvent;
        public TextView dateOfEvent;


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
            // date = (TextView) view.findViewById(R.id.date);
            //time = (TextView) view.findViewById(R.id.time);
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
                    Log.d("ATTENDING", "User ID: " + user.getUid());
                    //DatabaseManager manager = new DatabaseManager(user.getUid());.
                    event.respondGoing(user.getUid());
                    manager.modifyEvent(event);
                }
            });

            holder.notAttending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ATTENDING", "User ID: " + user.getUid());
                    //DatabaseManager manager = new DatabaseManager(user.getUid());.
                    event.respondDeclined(user.getUid());
                    manager.modifyEvent(event);
                }
            });


            List<String> friendsGoingIds = event.getFriendsGoingIds();
            List<Friend> friendsGoing = new ArrayList<>();
            final FriendsListAdapter friendsListAdapterAccepted = new FriendsListAdapter(friendsGoing,
                    applicationContext);
            FriendListView goingView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterAccepted.notifyDataSetChanged();
                }
            };
            manager.getEventFriendData(friendsGoingIds, friendsGoing, goingView);

            List<String> friendsNotGoingIds = event.getFriendsDeclinedIds();
            List<Friend> friendsNotGoing = new ArrayList<>();
            final FriendsListAdapter friendsListAdapterDeclined = new FriendsListAdapter(friendsNotGoing,
                    applicationContext);
            FriendListView notGoingView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterDeclined.notifyDataSetChanged();
                }
            };
            manager.getEventFriendData(friendsNotGoingIds, friendsNotGoing, notGoingView);

            List<String> friendsInvitedIds = event.getFriendsInvitedIds();
            List<Friend> friendsInvited = new ArrayList<>();
            final FriendsListAdapter friendsListAdapterInvited = new FriendsListAdapter(friendsInvited,
                    applicationContext);
            FriendListView invitedView = new FriendListView() {
                @Override
                public void friendDataChanged() {
                    friendsListAdapterInvited.notifyDataSetChanged();
                }
            };
            manager.getEventFriendData(friendsInvitedIds, friendsInvited, invitedView);

            //FriendsListAdapter friendsListAdapterInvited = new FriendsListAdapter(manager.getFriends(friendsInvited),
                   // applicationContext);

            holder.friendsAttending.setAdapter(friendsListAdapterAccepted);
            holder.friendsNotAttending.setAdapter(friendsListAdapterDeclined);
            holder.friendsInvited.setAdapter(friendsListAdapterInvited);


        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}