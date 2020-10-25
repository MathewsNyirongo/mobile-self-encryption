package com.example.mobileselfencryption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.JSONParser;
import com.example.mobileselfencryption.helpers.Message;
import com.example.mobileselfencryption.helpers.Note;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EncryptedNotesActivity extends AppCompatActivity {
    final String URL = "http://10.0.2.2/mobileselfencryption/notes.php";
    JSONParser jsonParser = new JSONParser();
    Note[] notes;
    String userId;
    TextView title;
    TableRow tableRow;
    TableLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted_notes);
        layout = (TableLayout) findViewById(R.id.notes);
        userId = getIntent().getStringExtra("USER_ID");
        AttemptGetNotes getNotes = new AttemptGetNotes();
        getNotes.execute(userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.plusbutton:
                Intent intent = new Intent(this, NewNoteActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AttemptGetNotes extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("user_id_fk", userId));
            JSONObject json = jsonParser.getNotes(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null) {
                    String date = null;
                    notes = new Note[jsonObject.getInt("notes")];
                    for (int i = 0; i < jsonObject.getInt("notes"); i++) {
                        JSONObject message = jsonObject.getJSONObject("note" + i);
                        notes[i] = new Note();
                        notes[i].setTitle(message.getString("note_title"));
                        title = new TextView(EncryptedNotesActivity.this);
                        title.setText(notes[i].getTitle());
                        final int finalI = i;
                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(EncryptedNotesActivity.this, NewNoteActivity.class);
                                intent.putExtra("TITLE", notes[finalI].getTitle());
                                intent.putExtra("TEXT", notes[finalI].getText());
                                startActivity(intent);
                            }
                        });
                        tableRow = new TableRow(EncryptedNotesActivity.this);
                        tableRow.addView(title);
//                        tableRow.addView(dateView);
                        layout.addView(tableRow);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}