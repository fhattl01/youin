package edu.tufts.cs.comp150.youin;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventHolder> {

    private List<Event> eventList;
    private FirebaseUser user;
    private String userId;
    private DatabaseManager manager;



    public class EventHolder extends RecyclerView.ViewHolder {
        public TextView name, description;// date, time;
        public Button attending, notAttending;

        public EventHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.eventListName);
            description = (TextView) view.findViewById(R.id.eventListDescription);
            attending = (Button) view.findViewById(R.id.attendingButton);
            notAttending = (Button) view.findViewById(R.id.notAttendingButton);
            user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();
            manager = new DatabaseManager(userId);
            // date = (TextView) view.findViewById(R.id.date);
            //time = (TextView) view.findViewById(R.id.time);
        }
    }


    public EventRecyclerViewAdapter(List<Event> eventList) {
        this.eventList = eventList;
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
            int backgroundColor = Color.parseColor("#0D47A1");
            int noDecisionColor = Color.parseColor("#F44336");
            holder.name.setText(event.getName());
            holder.description.setText(event.getDescription());
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
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}