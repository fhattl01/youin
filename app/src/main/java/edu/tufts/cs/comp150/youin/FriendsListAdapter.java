package edu.tufts.cs.comp150.youin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by franceshughes on 10/16/17.
 */

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    private ArrayList<Friend> friendList;
    Context mContext;

    private LayoutInflater inflater;

    public FriendsListAdapter(ArrayList<Friend> friend, Context context) {
        super(context, R.layout.friends_list_row, friend);
        this.friendList = friend;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = this.getItem(position);

        if (convertView == null) {
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friends_list_row, null);
        }

        TextView friendRow = (TextView)convertView.findViewById(R.id.friendListName);
        if (friend.getName() != null) {
            friendRow.setText(friend.getName());
        }
        TextView friendId = (TextView)convertView.findViewById(R.id.friendId);
        if (friend.getId() != null) {
            friendId.setText(friend.getId());
        }

        return convertView;
    }

}
