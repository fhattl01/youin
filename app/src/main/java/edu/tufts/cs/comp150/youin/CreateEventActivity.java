package edu.tufts.cs.comp150.youin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void navigateToWhat(View v) {
        EditText eventName = (EditText)findViewById(R.id.eventName);
        EditText eventDescription = (EditText)findViewById(R.id.eventDescription);

        eventName.setVisibility(View.VISIBLE);
        eventDescription.setVisibility(View.VISIBLE);

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

        timePicker.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);

        Button what = (Button)findViewById(R.id.createEventWhatButton);
        Button when = (Button)findViewById(R.id.createEventWhenButton);
        what.setBackgroundColor(getResources().getColor(R.color.blue));
        when.setBackgroundColor(getResources().getColor(R.color.white));

    }

    public void navigateToWhen(View v) {
        EditText eventName = (EditText)findViewById(R.id.eventName);
        EditText eventDescription = (EditText)findViewById(R.id.eventDescription);

        eventName.setVisibility(View.GONE);
        eventDescription.setVisibility(View.GONE);

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

        timePicker.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);

        Button what = (Button)findViewById(R.id.createEventWhatButton);
        Button when = (Button)findViewById(R.id.createEventWhenButton);
        what.setBackgroundColor(getResources().getColor(R.color.white));
        when.setBackgroundColor(getResources().getColor(R.color.blue));

    }

    /*
    public void navigateEventCreation(View v) {
        EditText eventName = (EditText)findViewById(R.id.eventName);
        EditText eventDescription = (EditText)findViewById(R.id.eventDescription);
        eventName.setVisibility(View.GONE);
        eventDescription.setVisibility(View.GONE);

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePicker.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);

        Button what = (Button)findViewById(R.id.createEventWhatButton);
        Button when = (Button)findViewById(R.id.createEventWhenButton);
        what.setBackgroundColor(getResources().getColor(R.color.white));
        when.setBackgroundColor(getResources().getColor(R.color.blue));

    }*/
}
