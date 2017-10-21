package edu.tufts.cs.comp150.youin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ManageFriendsActivity extends AppCompatActivity implements FriendListView {

    List<Friend> friendList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FriendsListAdapter friendsListAdapter;
    private DatabaseManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setContentView(R.layout.activity_manage_friends);

        ListView friendListView = (ListView)findViewById(R.id.manageFriendsList);

        ArrayList<Friend> friends = new ArrayList<>();
        friendsListAdapter = new FriendsListAdapter(friends, this);
        friendListView.setAdapter(friendsListAdapter);

        manager = new DatabaseManager(firebaseUser.getUid());
        manager.getFriendData(friends, this);
    }

    @Override
    public void friendDataChanged() {
        friendsListAdapter.notifyDataSetChanged();
    }

    public void submitFriendSearch(View v) {
        EditText entry = (EditText)findViewById(R.id.friendSearch);
        String query = entry.getText().toString();

        manager.searchFriends(query, this);
    }
}
