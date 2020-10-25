package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.JSONParser;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextInputEditText email, password;
    String URL = "https://10.0.2.2/mobileselfencryption/index.php";
    JSONParser jsonParser = new JSONParser();
    boolean isLoginSuccess = false;
    String userId = null;
    String messagesKey = null;
    String contactsKey = null;
    String notesKey = null;
    String filesKey = null;
    Boolean isActive = false;
    private Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUp(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void signIn(View view){
        email = (TextInputEditText) (findViewById(R.id.emailEditText));
        password = (TextInputEditText) (findViewById(R.id.passwordEditText));
        if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
            AttemptLogin attemptLogin = new AttemptLogin();
            attemptLogin.execute(email.getText().toString(), password.getText().toString());
        }else {
            Toast.makeText(getApplicationContext(), "Please fill out both the email address and the password", Toast.LENGTH_LONG).show();
        }
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String email = args[0];
            String password = args[1];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            JSONObject json = jsonParser.loginUserHttpRequest(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    if(Integer.valueOf(jsonObject.getString("success")) == 1){
                        isLoginSuccess = true;
                        userId = jsonObject.getString("user_id");
                        messagesKey = jsonObject.getString("messages_key");
                        contactsKey = jsonObject.getString("contacts_key");
                        notesKey = jsonObject.getString("notes_key");
                        isActive = jsonObject.getBoolean("isActive");
                        filesKey = jsonObject.getString("files_key");
                        if(isActive){
                            intent = new Intent(MainActivity.this, DashBoardActivity.class);
                            intent.putExtra("USER_ID", userId);
                            intent.putExtra("MESSAGES_KEY", messagesKey);
                            intent.putExtra("CONTACTS_KEY", contactsKey);
                            intent.putExtra("NOTES_KEY", notesKey);
                            intent.putExtra("FILES_KEY", filesKey);
                            startActivity(intent);
                            email.setText("");
                            password.setText("");
                        }else{
                            Toast.makeText(getApplicationContext(), "Account is locked", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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