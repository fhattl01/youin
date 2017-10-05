package edu.tufts.cs.comp150.youin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        CardView what = (CardView)findViewById(R.id.whatView);
        what.setVisibility(View.VISIBLE);
    }

    public void navigateToWhat(View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHAT);
    }

    public void navigateToWhen(View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHEN);
    }

    public void navigateToWho(View v) {

        handleEventAttributeCategoryChange(EventAttributeCategory.WHO);
        //test populate the list view
        String[] myStringArray = {"Frank", "Frances", "Fernando"};
        ArrayAdapter<String> testFriendsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myStringArray);
        ListView friendsList = (ListView) findViewById(R.id.inviteList);
        friendsList.setAdapter(testFriendsAdapter);

        Spinner min_people = (Spinner) findViewById(R.id.minPeopleSpinner);

        ArrayAdapter<CharSequence> people = ArrayAdapter.createFromResource(
                this,
                R.array.min_people_amount,
                android.R.layout.simple_spinner_item);

        people.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        min_people.setAdapter(people);
    }

    public void navigateToWhere (View v) {
        handleEventAttributeCategoryChange(EventAttributeCategory.WHERE);
    }


    private void handleEventAttributeCategoryChange(EventAttributeCategory changeTo) {
        Button what = (Button)findViewById(R.id.createEventWhatButton);
        Button when = (Button)findViewById(R.id.createEventWhenButton);
        Button who = (Button)findViewById(R.id.createEventWhoButton);
        Button where = (Button)findViewById(R.id.createEventWhereButton);

        what.setBackgroundColor(getResources().getColor(R.color.white));
        when.setBackgroundColor(getResources().getColor(R.color.white));
        who.setBackgroundColor(getResources().getColor(R.color.white));
        where.setBackgroundColor(getResources().getColor(R.color.white));

        CardView whatCard = (CardView)findViewById(R.id.whatView);
        CardView whenCard = (CardView)findViewById(R.id.whenView);
        CardView whoCard = (CardView)findViewById(R.id.whoView);
        CardView whereCard = (CardView)findViewById(R.id.whereView);
        whatCard.setVisibility(View.GONE);
        whenCard.setVisibility(View.GONE);
        whoCard.setVisibility(View.GONE);
        whereCard.setVisibility(View.GONE);

        Button backButton = (Button)findViewById(R.id.backButton);
        Button nextButton = (Button)findViewById(R.id.nextButton);
        Button sendInvite = (Button)findViewById(R.id.sendInvite);

        switch (changeTo) {
            case WHAT:
                what.setBackgroundColor(getResources().getColor(R.color.blue));
                whatCard.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.GONE);
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhen(v);
                    }
                });
                break;
            case WHEN:
                when.setBackgroundColor(getResources().getColor(R.color.blue));
                whenCard.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhat(v);
                    }
                });
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWho(v);
                    }
                });
                break;
            case WHO:
                who.setBackgroundColor(getResources().getColor(R.color.blue));
                whoCard.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhen(v);
                    }
                });
                sendInvite.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWhere(v);
                    }
                });
                break;
            case WHERE:
                where.setBackgroundColor(getResources().getColor(R.color.blue));
                whereCard.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        navigateToWho(v);
                    }
                });
                sendInvite.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);

                break;
        }
    }
    public void sendInvite(View view) {
        TextView feedback = (TextView) findViewById(R.id.feedback);
        feedback.setText("your Invitation has been sent!");
    }
}
