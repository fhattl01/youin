package edu.tufts.cs.comp150.youin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventHolder> {

    private List<Event> eventList;

    public class EventHolder extends RecyclerView.ViewHolder {
        public TextView name, description;// date, time;

        public EventHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.eventListName);
            description = (TextView) view.findViewById(R.id.eventListDescription);
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
    public void onBindViewHolder(EventHolder holder, int position) {

        Event event = eventList.get(position);
        if (event != null) {
            holder.name.setText(event.getName());
            holder.description.setText(event.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}