package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobileselfencryption.addkeys.AddContactKeyActivity;
import com.example.mobileselfencryption.addkeys.AddFileKeyActivity;
import com.example.mobileselfencryption.addkeys.AddMessageKeyActivity;
import com.example.mobileselfencryption.addkeys.AddNotesKeyActivity;

public class SettingsActivity extends AppCompatActivity {
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userId = getIntent().getStringExtra("USER_ID");
    }

    public void addMessageKey(View view){
        startActivity(new Intent(this, AddMessageKeyActivity.class).putExtra("USER_ID", userId));
    }

    public void addContactKey(View view){
        startActivity(new Intent(this, AddContactKeyActivity.class).putExtra("USER_ID", userId));
    }

    public void addNotesKey(View view){
        startActivity(new Intent(this, AddNotesKeyActivity.class).putExtra("USER_ID", userId));
    }

    public void addFileKey(View view){
        startActivity(new Intent(this, AddFileKeyActivity.class).putExtra("USER_ID", userId));
    }
}