package edu.tufts.cs.comp150.youin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franceshughes on 10/16/17.
 */

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    private List<Friend> friendList;
    private FirebaseUser user;
    Context mContext;

    private LayoutInflater inflater;

    private static class ViewHolder {
        TextView friendName;
        TextView friendId;
        TextView friendUsername;
        Button addFriend;
    }

    //boolean friend = false;

    public FriendsListAdapter(List<Friend> friend, Context context) {
        super(context, R.layout.friends_list_row, friend);
        this.friendList = friend;
        this.mContext = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend friend = this.getItem(position);
        FriendsListAdapter.ViewHolder viewHolder;
        final View result;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_row, parent, false);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.nameOfFriend);
            viewHolder.friendId = (TextView) convertView.findViewById(R.id.idOfFriend);
            viewHolder.addFriend = (Button) convertView.findViewById(R.id.addFriendButton);
            viewHolder.friendUsername = (TextView) convertView.findViewById(R.id.usernameOfFriend);

            result = convertView;
            convertView.setTag(viewHolder);


            //inflater = LayoutInflater.from(getContext());
            //.convertView = inflater.inflate(R.layout.friends_list_row, null);
        } else {
            viewHolder = (FriendsListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
            Log.d("FRIEND", friend.getName());

            viewHolder.friendName.setText(friend.getName());
            viewHolder.friendId.setText(friend.getId());
            viewHolder.friendUsername.setText(friend.getUsername());
            // Log.d("INVITE", "Friend List: " + friend.getName() + ": " + friend.getInvited());
            if (friend.isFriend()) {
                viewHolder.addFriend.setVisibility(View.GONE);
            } else {
                viewHolder.addFriend.setVisibility(View.VISIBLE);
                viewHolder.addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseManager manager = new DatabaseManager(user.getUid());
                        Log.d("FRIEND", "Friend ID: " + friend.getId());
                        manager.addFriend(friend.getId());
                    }
                });
            }

        return result;
    }
}


      /*  TextView friendRow = (TextView)convertView.findViewById(R.id.nameOfFriend);
        if (friend.getName() != null) {
            friendRow.setText(friend.getName());
        }
        TextView friendId = (TextView)convertView.findViewById(R.id.idOfFriend);
        if (friend.getId() != null) {
            friendId.setText(friend.getId());
        }

        return convertView;*/