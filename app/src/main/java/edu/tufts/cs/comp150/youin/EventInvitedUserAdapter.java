package edu.tufts.cs.comp150.youin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by maggi on 10/26/17.
 */

public class EventInvitedUserAdapter extends ArrayAdapter<Friend> {
    private List<Friend> friendList;
    private FirebaseUser user;
    Context mContext;

    private LayoutInflater inflater;

    private static class ViewHolder {
        TextView friendName;
        TextView friendUsername;
    }

    //boolean friend = false;

    public EventInvitedUserAdapter(List<Friend> friend, Context context) {
        super(context, R.layout.friends_list_row, friend);
        this.friendList = friend;
        this.mContext = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend friend = this.getItem(position);
        ViewHolder viewHolder;
        final View result;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_invited_user_row, parent, false);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.eventInvitedName);
            viewHolder.friendUsername = (TextView) convertView.findViewById(R.id.eventInvitedUsername);

            result = convertView;
            convertView.setTag(viewHolder);


            //inflater = LayoutInflater.from(getContext());
            //.convertView = inflater.inflate(R.layout.friends_list_row, null);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.friendName.setText(friend.getName());
        viewHolder.friendUsername.setText(friend.getUsername());

        return result;
    }
}
