package com.example.mobileselfencryption;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileselfencryption.helpers.JSONParser;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText fullName, phoneNumber, password, email, passwordConfirm;
    String URL = "https://10.0.2.2/mobileselfencryption/index.php";
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void sendUserInformation(View view) {
        fullName = (TextInputEditText)findViewById(R.id.fullNameEditText);
        phoneNumber = (TextInputEditText)findViewById(R.id.phoneNumberEditText);
        email = (TextInputEditText)findViewById(R.id.emailEditText);
        password = (TextInputEditText)findViewById(R.id.passwordEditText);
        passwordConfirm = (TextInputEditText)findViewById(R.id.confirmPasswordEditText);
        if(password.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Passwords must be at least 8 characters long", Toast.LENGTH_LONG).show();
        }else {
            if (password.getText().toString().equals(passwordConfirm.getText().toString())) {
                AttemptRegister attemptRegister = new AttemptRegister();
                attemptRegister.execute(fullName.getText().toString(), phoneNumber.getText().toString(), email.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "The passwords do not match", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AttemptRegister extends AsyncTask<String, String, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String fullName = args[0];
            String phoneNumber = args[1];
            String email = args[2];
            String password = args[3];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("full_name", fullName));
            params.add(new BasicNameValuePair("phone_number", phoneNumber));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            JSONObject jsonObject = jsonParser.registerUserHttpRequest(URL, params);
            return jsonObject;
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