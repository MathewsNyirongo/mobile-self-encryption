package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.JSONParser;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity {
    final String URL = "http://10.0.2.2/mobileselfencryption/notes.php";
    String userId;
    JSONParser jsonParser = new JSONParser();
    TextInputEditText titleInput;
    EditText textInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        titleInput = (TextInputEditText) findViewById(R.id.titleEditText);
        titleInput.setText(getIntent().getStringExtra("TITLE"));
        textInput = (EditText) findViewById(R.id.textEditText);
        titleInput.setText(getIntent().getStringExtra("TEXT"));
        userId = getIntent().getStringExtra("USER_ID");
    }

    public void saveNote(View view){
        AttemptEncryptNote encryptNote = new AttemptEncryptNote();
        encryptNote.execute(titleInput.getText().toString(), textInput.getText().toString(), String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
    }

    public void cancelNote(View view){
        NewNoteActivity.this.finish();
    }

    private class AttemptEncryptNote extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            String title = args[0];
            String text = args[1];
            String timeStamp = args[2];
            params.add(new BasicNameValuePair("note_title", title));
            params.add(new BasicNameValuePair("user_id_fk", userId));
            params.add(new BasicNameValuePair("note_text", text));
            params.add(new BasicNameValuePair("date_time", timeStamp));
            JSONObject json = jsonParser.encryptNote(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }
}