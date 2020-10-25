package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.JSONParser;
import com.example.mobileselfencryption.helpers.Message;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayMessages extends AppCompatActivity {

    String url = "http://10.0.2.2/mobileselfencryption/messages.php";
    TableLayout layout;
    TableRow tableRow;
    TextView name;
    TextView textMessage;
    String userId;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_messages);
        layout = (TableLayout) findViewById(R.id.inbox);
        userId = getIntent().getStringExtra("USER_ID");
        List<Message> messages = getAllSms();
        for (final Message message : messages) {
            name = new TextView(DisplayMessages.this);
            textMessage = new TextView(DisplayMessages.this);
            name.setText(getContactbyPhoneNumber(getApplicationContext(), message.getAddress()));
            name.setClickable(true);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AttemptEncryptMessage encryptMessage = new AttemptEncryptMessage();
                    encryptMessage.execute(message.getAddress(), message.getTime(), message.getMsg());
                }
            });
            textMessage.setText(message.getMsg());
            tableRow = new TableRow(DisplayMessages.this);
            tableRow.addView(name);
            tableRow.addView(textMessage);
            layout.addView(tableRow);
        }
    }

    public List<Message> getAllSms() {
        List<Message> lstSms = new ArrayList<Message>();
        Message objSms;
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        this.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Message();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                Log.e("Time", objSms.getTime());
                lstSms.add(objSms);
                c.moveToNext();
            }
        }else {
            throw new RuntimeException("You have no SMS");
        }
        c.close();
        return lstSms;
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

    private class AttemptEncryptMessage extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            String phoneNumber = args[0];
            String time = args[1];
            String textMessage = args[2];
            params.add(new BasicNameValuePair("message_sender", phoneNumber));
            params.add(new BasicNameValuePair("user_id_fk", userId));
            params.add(new BasicNameValuePair("message_text", textMessage));
            params.add(new BasicNameValuePair("date_time", time));
            JSONObject json = jsonParser.encryptMessage(url, params);
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