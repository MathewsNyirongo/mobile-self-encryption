package com.example.mobileselfencryption.addkeys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.mobileselfencryption.R;
import com.example.mobileselfencryption.helpers.JSONParser;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddContactKeyActivity extends AppCompatActivity {
    TextInputEditText newKey, confirmNewKey;
    String URL = "https://10.0.2.2/mobileselfencryption/keys.php";
    JSONParser jsonParser = new JSONParser();
    String userId;
    boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts_key);
        userId = getIntent().getStringExtra("USER_ID");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.85), (int) (height*.55));
    }

    public void cancelActivity(View view){
        Intent intent = new Intent(getApplicationContext(), AddContactKeyActivity.class);
        AddContactKeyActivity.this.finish();
    }

    public void saveKey(View view){
        newKey = (TextInputEditText) findViewById(R.id.newKey);
        confirmNewKey = (TextInputEditText) findViewById(R.id.confirmNewKey);
        if (newKey.getText().toString().length() == 4){
            if(newKey.getText().toString().equals(confirmNewKey.getText().toString())){
                new AttemptAddKey().execute(newKey.getText().toString(), userId);
            }else {
                Toast.makeText(getApplicationContext(), "The keys do not match", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Key must be exactly 4 characters long", Toast.LENGTH_LONG).show();
        }
        if(isSuccess){
            newKey.setText("");
            confirmNewKey.setText("");
            AddContactKeyActivity.this.finish();
        }
    }

    private class AttemptAddKey extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String newContactsKey = args[0];
            String userId = args[1];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("contacts_key", newContactsKey));
            params.add(new BasicNameValuePair("user_id_fk", userId));
            JSONObject jsonObject = jsonParser.addNewKey(URL, params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if(Integer.valueOf(jsonObject.getString("success")) == 1){
                        isSuccess = true;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }
}