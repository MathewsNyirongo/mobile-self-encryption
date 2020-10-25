package com.example.mobileselfencryption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileselfencryption.helpers.Contact;
import com.example.mobileselfencryption.helpers.File;
import com.example.mobileselfencryption.helpers.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EncryptedFilesActivity extends AppCompatActivity {
    String URL = "https://10.0.2.2/mobileselfencryption/files.php";
    String userId;
    JSONParser jsonParser = new JSONParser();
    File[] files;
    TextView imageTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted_files);
        userId = getIntent().getStringExtra("USER_ID");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.plusbutton:
                Intent intent = new Intent(this, FileTypeActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private class AttemptGetFiles extends AsyncTask<String, Void, JSONObject> {
//        ProgressDialog pd;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd = ProgressDialog.show(EncryptedFilesActivity.this, "Loading...", null, true, true);
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            ArrayList params = new ArrayList();
//            params.add(new BasicNameValuePair("user_id_fk", userId));
//            JSONObject json = jsonParser.getFiles(URL, params);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            try{
//                if(jsonObject != null){
//                    files = new File[jsonObject.getInt("files")];
//                    for(int i=0; i<jsonObject.getInt("files"); i++){
//                        JSONObject file = jsonObject.getJSONObject("file"+i);
//                        files[i] = new File();
//                        //files[i].setFile((BitmapFactory.decodeStream(file.getString("file")));
//                        imageTextView = new TextView(EncryptedFilesActivity.this);
//                        name.setText(contacts[i].getContactName());
//                        ImageView call = new ImageView(EncryptedContactsActivity.this);
//                        ImageView delete = new ImageView(EncryptedContactsActivity.this);
//                        call.setImageResource(R.drawable.ic_baseline_call_24);
//                        call.setClickable(true);
//                        final int finalI = i;
//                        call.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contacts[finalI].getContactNumber1(), null));
//                                startActivity(callIntent);
//                            }
//                        });
//                        delete.setImageResource(R.drawable.ic_baseline_delete_24);
//                        delete.setClickable(true);
//                        delete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                EncryptedContactsActivity.AttemptGetContacts.AttemptDeleteContact deleteContact = new EncryptedContactsActivity.AttemptGetContacts.AttemptDeleteContact();
//                                deleteContact.execute(contacts[finalI].getId());
//                                layout.removeAllViews();
//                                EncryptedContactsActivity.AttemptGetContacts getContacts = new EncryptedContactsActivity.AttemptGetContacts();
//                                getContacts.execute(userId);
//                            }
//                        });
//                        tableRow = new TableRow(EncryptedContactsActivity.this);
//                        tableRow.addView(name);
//                        tableRow.addView(call);
//                        tableRow.addView(delete);
//                        layout.addView(tableRow);
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private class AttemptDeleteContact extends AsyncTask<String, String, JSONObject>{
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... args) {
//                ArrayList params = new ArrayList();
//                String id = args[0];
//                params.add(new BasicNameValuePair("id", id));
//                JSONObject json = jsonParser.deleteContact(URL, params);
//                return json;
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                try{
//                    if(jsonObject!=null){
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}