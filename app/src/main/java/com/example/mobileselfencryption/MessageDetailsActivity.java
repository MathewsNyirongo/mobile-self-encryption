package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class MessageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        TextView sender = (TextView) findViewById(R.id.senderView);
        sender.setText(getIntent().getStringExtra("SENDER"));
        TextView dateTime = (TextView) findViewById(R.id.dateTimeView);
        dateTime.setText(getIntent().getStringExtra("DATE_TIME"));
        TextView message = (TextView) findViewById(R.id.messageView);
        message.setText(getIntent().getStringExtra("MESSAGE"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7), (int) (height*.3));
    }
}