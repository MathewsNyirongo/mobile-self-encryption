package com.example.mobileselfencryption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.Contact;
import com.example.mobileselfencryption.helpers.JSONParser;
import com.example.mobileselfencryption.helpers.Message;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EncryptedMessagesActivity extends AppCompatActivity {
    final String URL = "http://10.0.2.2/mobileselfencryption/messages.php";
    JSONParser jsonParser = new JSONParser();
    Message[] messages;
    String userId;
    TextView name;
    TableRow tableRow;
    TableLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted_messages);
        layout = (TableLayout) findViewById(R.id.messages);
        userId = getIntent().getStringExtra("USER_ID");
        AttemptGetMessages getMessages = new AttemptGetMessages();
        getMessages.execute(userId);
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
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(this, DisplayMessages.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                }else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 3004);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AttemptGetMessages extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("user_id_fk", userId));
            JSONObject json = jsonParser.getMessages(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null) {
                    String date = null;
                    messages = new Message[jsonObject.getInt("messages")];
                    for (int i = 0; i < jsonObject.getInt("messages"); i++) {
                        JSONObject message = jsonObject.getJSONObject("message" + i);
                        messages[i] = new Message();
                        messages[i].setAddress(message.getString("message_sender"));
                        messages[i].setContactName(getContactbyPhoneNumber(getApplicationContext(), message.getString("message_sender")));
                        messages[i].setMsg(message.getString("message_text"));
                        messages[i].setTime(message.getString("date_time"));
                        name = new TextView(EncryptedMessagesActivity.this);
                        name.setText(messages[i].getContactName());
                        final int finalI = i;
                        name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(EncryptedMessagesActivity.this, MessageDetailsActivity.class);
                                intent.putExtra("SENDER", messages[finalI].getContactName());
                                intent.putExtra("DATE_TIME", new SimpleDateFormat("yyyy/mm/dd hh:mma").format(new Date(Long.valueOf(messages[finalI].getTime()))));
                                intent.putExtra("MESSAGE", messages[finalI].getMsg());
                                startActivity(intent);
                            }
                        });
                        date = new SimpleDateFormat("MMM d", Locale.ENGLISH).format(new Date(Long.valueOf(messages[i].getTime())));
                        TextView dateView = new TextView(EncryptedMessagesActivity.this);
                        dateView.setText(date);
                        tableRow = new TableRow(EncryptedMessagesActivity.this);
                        tableRow.addView(name);
                        tableRow.addView(dateView);
                        layout.addView(tableRow);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String getContactbyPhoneNumber(Context c, String phoneNumber) {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cursor = c.getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) {
                return phoneNumber;
            }else {
                String name = phoneNumber;
                try {

                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }

                } finally {
                    cursor.close();
                }

                return name;
            }
        }
    }
}