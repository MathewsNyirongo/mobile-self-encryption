package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class DashBoardActivity extends AppCompatActivity {
    String userId;
    String messagesKey = null;
    String contactsKey = null;
    String notesKey = null;
    String filesKey = null;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        userId = getIntent().getStringExtra("USER_ID");
        messagesKey = getIntent().getStringExtra("MESSAGES_KEY");
        contactsKey = getIntent().getStringExtra("CONTACTS_KEY");
        notesKey = getIntent().getStringExtra("NOTES_KEY");
        filesKey = getIntent().getStringExtra("FILES_KEY");
    }

    public void onContactsClicked(View view){
        if(!contactsKey.isEmpty()){
            if(contactsKey.length() == 4){
                intent = new Intent(this, EnterKeyActivity.class);
                intent.putExtra("CONTACTS_KEY", contactsKey);
            }else {
                intent = new Intent(this, AddKey.class);
            }
        }else{
            intent = new Intent(this, AddKey.class);
        }
        intent.putExtra("VIEW_CLICKED", "contacts");
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    public void onMessagesClicked(View view){
        if(!messagesKey.isEmpty()){
            if(messagesKey.length() == 4){
                intent = new Intent(this, EnterKeyActivity.class);
                intent.putExtra("MESSAGES_KEY", messagesKey);
            }else {
                intent = new Intent(this, AddKey.class);
            }
        }else{
            intent = new Intent(this, AddKey.class);
        }
        intent.putExtra("USER_ID", userId);
        intent.putExtra("VIEW_CLICKED", "messages");
        startActivity(intent);
    }

    public void onNotesClicked(View view){
        if(!notesKey.isEmpty()){
            if(notesKey.length() == 4){
                intent = new Intent(this, EnterKeyActivity.class);
                intent.putExtra("NOTES_KEY", notesKey);
            }else {
                intent = new Intent(this, AddKey.class);
            }
        }else{
            intent = new Intent(this, AddKey.class);
        }
        intent.putExtra("USER_ID", userId);
        intent.putExtra("VIEW_CLICKED", "notes");
        startActivity(intent);
    }

    public void onFilesClicked(View view){
        if(filesKey != null){
            if(filesKey.length() == 4){
                intent = new Intent(this, EnterKeyActivity.class);
                intent.putExtra("FILES_KEY", filesKey);
            }else {
                intent = new Intent(this, AddKey.class);
            }
        }else{
            intent = new Intent(this, AddKey.class);
        }
        intent.putExtra("USER_ID", userId);
        intent.putExtra("VIEW_CLICKED", "files");
        startActivity(intent);
    }

    public void settings(View view){
        intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("FILES_KEY", filesKey);
        intent.putExtra("NOTES_KEY", notesKey);
        intent.putExtra("MESSAGES_KEY", messagesKey);
        intent.putExtra("CONTACTS_KEY", contactsKey);
        startActivity(intent);
    }

    public void  logout(View view){ DashBoardActivity.this.finish(); }
}