package edu.tufts.cs.comp150.youin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franceshughes on 10/23/17.
 */

public class InviteListAdapter extends ArrayAdapter<Friend> {

    private List<Friend> friendList;
    private Context mContext;

    //private LayoutInflater inflater;

    private static class ViewHolder {
        TextView friendName;
        TextView friendId;
        CheckBox checkBox;
    }

    public InviteListAdapter(List<Friend> friend, Context context) {
        super(context, R.layout.invite_friends_row, friend);
        this.friendList = friend;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = this.getItem(position);
        ViewHolder viewHolder;
        final View result;


        if (convertView == null) {
            viewHolder = new ViewHolder();

            //inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_friends_row, parent, false);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.friendListName);
            viewHolder.friendId = (TextView) convertView.findViewById(R.id.friendId);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.friendCheck);

            result = convertView;
            convertView.setTag(viewHolder);
            //convertView = inflater.inflate(R.layout.invite_friends_row, null);
            //convertView.
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.friendName.setText(friend.getName());
        viewHolder.friendId.setText(friend.getId());
        Log.d("INVITE", "Friend List: " + friend.getName() + ": " + friend.getInvited());
        viewHolder.checkBox.setChecked(friend.getInvited());

        return result;
        /*
        TextView friendRow = (TextView)convertView.findViewById(R.id.friendListName);
        if (friend.getName() != null) {
            friendRow.setText(friend.getName());
        }
        TextView friendId = (TextView)convertView.findViewById(R.id.friendId);
        if (friend.getId() != null) {
            friendId.setText(friend.getId());
        }

        Boolean invite = (Boolean.getBoolean(convertView.findViewById(R.id.)))
        if (friend.getInvited() == true) {
            friend
        }
        return convertView;*/
    }
}




