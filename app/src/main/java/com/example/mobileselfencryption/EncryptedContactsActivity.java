package com.example.mobileselfencryption;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mobileselfencryption.helpers.Contact;
import com.example.mobileselfencryption.helpers.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EncryptedContactsActivity extends AppCompatActivity {
    public static final int REQUEST_READ_CONTACTS = 79;
    ArrayList<String> phoneNumbers = new ArrayList<>();
    String contactName = null;
    String userId = null;
    String URL = "https://10.0.2.2/mobileselfencryption/contacts.php";
    JSONParser jsonParser = new JSONParser();
    Contact[] contacts;
    TableLayout layout;
    TableRow tableRow;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted_contacts);
        userId = getIntent().getStringExtra("USER_ID");
        layout = (TableLayout) findViewById(R.id.encryptedContacts);
        AttemptGetContacts attemptGetContacts = new AttemptGetContacts();
        attemptGetContacts.execute(userId);


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
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                    openContacts();
                }else {
                    requestPermission();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver resolver = getContentResolver();
        if(resultCode == Activity.RESULT_OK){
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            if(cursor.moveToFirst()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                while (phones.moveToNext()){
                    contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNumbers.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                }
            }
            AttemptEncryptContact encryptContact = new AttemptEncryptContact();
            encryptContact.execute(contactName);
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    public void openContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private class AttemptEncryptContact extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("contact_name", contactName));
            params.add(new BasicNameValuePair("user_id_fk", userId));
            for(int i=0; i<phoneNumbers.size(); i++){
                params.add(new BasicNameValuePair(String.format("contact_number%d", i+1), phoneNumbers.get(i)));
            }
            JSONObject json = jsonParser.encryptContact(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    contactName="";
                    phoneNumbers.clear();
                    layout.removeAllViews();
                    AttemptGetContacts getContacts = new AttemptGetContacts();
                    getContacts.execute(userId);
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }

    private class AttemptGetContacts extends AsyncTask<String, String, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("user_id_fk", userId));
            JSONObject json = jsonParser.getContacts(URL, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    contacts = new Contact[jsonObject.getInt("contacts")];
                    for(int i=0; i<jsonObject.getInt("contacts"); i++){
                        JSONObject user = jsonObject.getJSONObject("user"+i);
                        contacts[i] = new Contact(user.getString("id"), user.getString("contact_name"), user.getString("contact_number1"), user.getString("contact_number2"), user.getString("contact_number3"), user.getString("contact_number4"), user.getString("contact_number5"));
                        name = new TextView(EncryptedContactsActivity.this);
                        name.setText(contacts[i].getContactName());
                        ImageView call = new ImageView(EncryptedContactsActivity.this);
                        ImageView delete = new ImageView(EncryptedContactsActivity.this);
                        call.setImageResource(R.drawable.ic_baseline_call_24);
                        call.setClickable(true);
                        final int finalI = i;
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contacts[finalI].getContactNumber1(), null));
                                startActivity(callIntent);
                            }
                        });
                        delete.setImageResource(R.drawable.ic_baseline_delete_24);
                        delete.setClickable(true);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AttemptDeleteContact deleteContact = new AttemptDeleteContact();
                                deleteContact.execute(contacts[finalI].getId());
                                layout.removeAllViews();
                                AttemptGetContacts getContacts = new AttemptGetContacts();
                                getContacts.execute(userId);
                            }
                        });
                        tableRow = new TableRow(EncryptedContactsActivity.this);
                        tableRow.addView(name);
                        tableRow.addView(call);
                        tableRow.addView(delete);
                        layout.addView(tableRow);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private class AttemptDeleteContact extends AsyncTask<String, String, JSONObject>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... args) {
                ArrayList params = new ArrayList();
                String id = args[0];
                params.add(new BasicNameValuePair("id", id));
                JSONObject json = jsonParser.deleteContact(URL, params);
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try{
                    if(jsonObject!=null){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}