package edu.tufts.cs.comp150.youin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageFriendsActivity extends AppCompatActivity {

    ArrayList<Friend> friendList;
    ListView listView;

    private static FriendsListAdapter friendsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        listView = (ListView)findViewById(R.id.friend_listview);

        friendList = new ArrayList<>();

        friendList.add(new Friend("Frances"));

        friendsListAdapter = new FriendsListAdapter(getApplicationContext(), friendList);
    }
}
