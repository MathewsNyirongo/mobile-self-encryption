package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.mobileselfencryption.addkeys.AddContactKeyActivity;
import com.example.mobileselfencryption.addkeys.AddFileKeyActivity;
import com.example.mobileselfencryption.addkeys.AddMessageKeyActivity;
import com.example.mobileselfencryption.addkeys.AddNotesKeyActivity;

public class AddKey extends AppCompatActivity {
    String userId = null;
    String viewClicked = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);
        userId = getIntent().getStringExtra("USER_ID");
        viewClicked = getIntent().getStringExtra("VIEW_CLICKED");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7), (int) (height*.3));
    }

    public void cancelActivity(View view){
        AddKey.this.finish();
    }

    public void setKey(View view){
        Intent intent = null;
        switch (viewClicked){
            case "contacts":
                intent = new Intent(this, AddContactKeyActivity.class);
                break;
            case "messages":
                intent = new Intent(this, AddMessageKeyActivity.class);
                break;
            case "notes":
                intent = new Intent(this, AddNotesKeyActivity.class);
                break;
            case "files":
                intent = new Intent(this, AddFileKeyActivity.class);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Unknown activity selected", Toast.LENGTH_SHORT).show();
                break;
        }
        if(intent != null){
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        }
        AddKey.this.finish();
    }
}