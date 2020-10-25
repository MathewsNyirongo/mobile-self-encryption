package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class EnterKeyActivity extends AppCompatActivity {
    String userId = null;
    String viewClicked = null;
    String contactsKey = null;
    String messagesKey = null;
    String notesKey = null;
    String filesKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_key);
        userId = getIntent().getStringExtra("USER_ID");
        viewClicked = getIntent().getStringExtra("VIEW_CLICKED");
        contactsKey = getIntent().getStringExtra("CONTACTS_KEY");
        messagesKey = getIntent().getStringExtra("MESSAGES_KEY");
        notesKey = getIntent().getStringExtra("NOTES_KEY");
        filesKey = getIntent().getStringExtra("FILES_KEY");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.85), (int) (height*.35));
    }

    public void cancelActivity(View view){
        EnterKeyActivity.this.finish();
    }

    public void onProceedClicked(View view){
        TextInputEditText enteredKey = (TextInputEditText) findViewById(R.id.key);
        if(enteredKey.getText().toString().length()>0){
            Intent intent = null;
            switch (viewClicked){
                case "contacts":
                    if(enteredKey.getText().toString().equals(contactsKey)){
                        intent = new Intent(this, EncryptedContactsActivity.class);
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "messages":
                    if(enteredKey.getText().toString().equals(messagesKey)){
                        intent = new Intent(this, EncryptedMessagesActivity.class);
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "notes":
                    if(enteredKey.getText().toString().equals(notesKey)){
                        intent = new Intent(this, EncryptedNotesActivity.class);
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "files":
                    if(enteredKey.getText().toString().equals(filesKey)){
                        intent = new Intent(this, EncryptedFilesActivity.class);
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Unknown activity selected", Toast.LENGTH_SHORT).show();
                    break;
            }
            if(intent != null){
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                EnterKeyActivity.this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(), "You have not entered a key", Toast.LENGTH_LONG).show();
        }
    }
}