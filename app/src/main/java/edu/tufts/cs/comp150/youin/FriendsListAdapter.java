package edu.tufts.cs.comp150.youin;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by franceshughes on 10/15/17.
 */

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    private List<Friend> friendList;
    Context mContext;

    public FriendsListAdapter(Context context, List<Friend> friends) {
        super(context, R.layout.friends_list_row, friends);
        this.friendList = friends;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friends_list_row, null);
        }

        Friend friend = getItem(position);
        TextView textViewItem = (TextView) v.findViewById(R.id.friendListName);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.friendCheck);
        textViewItem.setText(friend.getName());
        textViewItem.setText(friend.getId());

        return v;
    }

}

