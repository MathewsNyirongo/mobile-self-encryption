package com.example.mobileselfencryption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.mobileselfencryption.helpers.File;
import com.example.mobileselfencryption.helpers.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileTypeActivity extends AppCompatActivity {

    String URL = "https://10.0.2.2/mobileselfencryption/files.php";
    String userId;
    Uri picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
    JSONParser jsonParser = new JSONParser();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_type);
        userId = getIntent().getStringExtra("USER_ID");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void pickImage(View view){
        try{
            if(ActivityCompat.checkSelfPermission(FileTypeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(FileTypeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }else{
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an Image"), 1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            selectedImage = data.getData();
            Log.e("img", selectedImage.toString());
            try {
                photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);

//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePath = cursor.getString(columnIndex);
//                cursor.close();
//                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
//                byte[] bytes = byteArrayOutputStream.toByteArray();
//                ba1 = Base64.encodeToString(bytes, 0);
//                AttemptEncryptFile encryptFile = new AttemptEncryptFile();
//                encryptFile.execute();
                new MyUploader(FileTypeActivity.this).upload(userId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("Error", "Failed");
        }
    }

    public String getImagePath(Uri uri){
        String[] pojection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, pojection, null, null, null);
        if(cursor == null){return null;}
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    public class MyUploader{
        private final String DATA_UPLOAD_URL = "https://10.0.2.2/mobileselfencryption/files.php";
        private final Context context;
        public MyUploader(Context context){this.context=context;}


        public void upload(final String...params){
                java.io.File imageFile;
                try {
                    imageFile = new java.io.File(getImagePath(selectedImage));
                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(context, "Please pick an image fro the right source: "+ex.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                AndroidNetworking.upload(DATA_UPLOAD_URL)
                        .addMultipartFile("image", imageFile)
                        .addMultipartParameter("name", "upload")
                        .setTag("MYSQL_UPLOAD")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(response != null){
                                    try {
                                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        if(response.getInt("success") == 1){
                                            String fileName = params[0];
                                        }else{
                                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                        Toast.makeText(context, "JSONException "+ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(context, "Null response...", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(ANError anError) {
                                anError.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "Error: "+anError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
        }
    }

    private class AttemptEncryptFile extends AsyncTask<Void, Void, JSONObject>{
        private ProgressDialog pd = new ProgressDialog(FileTypeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Uploading...please wait");
            pd.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("user_id_fk", userId));
            nameValuePairs.add(new BasicNameValuePair("file", System.currentTimeMillis()+".jpg"));
            return jsonParser.encryptImage(URL, nameValuePairs);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            pd.hide();
            pd.dismiss();
            if(jsonObject != null){
                try {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from the server", Toast.LENGTH_LONG).show();
            }
        }
    }
}