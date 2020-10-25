package com.example.mobileselfencryption.helpers;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class JSONParser {
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    static String error = "";

    public  JSONObject encryptImage(String url, ArrayList params){
        return executePostRequest(url, params);
    }
    public JSONObject encryptNote(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject getNotes(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject encryptMessage(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject getMessages(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject encryptContact(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject getContacts(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject deleteContact(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    private JSONObject executePostRequest(String url, ArrayList params){
        HttpClient client = createHttpClient();
        HttpResponse response = null;
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            response = client.execute(post);

            error = String.valueOf(response.getStatusLine().getStatusCode());
            inputStream = response.getEntity().getContent();

            json = convertStreamToString(inputStream);
            jsonObject = new JSONObject(json);
            jsonObject.put("error_code", error);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject addNewKey(String url, ArrayList params){
        try{
            HttpClient httpClient = createHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            error = String.valueOf(httpResponse.getStatusLine().getStatusCode());
            inputStream = httpResponse.getEntity().getContent();
        }catch (Exception ex){
            Log.e("error: ", ex.toString());
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();
        }catch (Exception ex){
            Log.e("Buffer error", "Error converting result " + ex.toString());
        }

        try{
            jsonObject = new JSONObject(json);
            jsonObject.put("error_code", error);
        }catch (JSONException ex){
            Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }
        return jsonObject;
    }

    public JSONObject registerUserHttpRequest(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    public JSONObject loginUserHttpRequest(String url, ArrayList params){
        return executePostRequest(url, params);
    }

    private HttpClient createHttpClient(){
        KeyStore trustStore = null;
        HttpClient httpClient = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sslSocketFactory = new MySSLSocketFactory(trustStore);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams parameters = new BasicHttpParams();
            HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(parameters, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslSocketFactory, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(parameters, registry);

            httpClient = new DefaultHttpClient(ccm, parameters);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return httpClient;
    }

    private String convertStreamToString(InputStream inputStream){
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            String line = null;
            while (true){
                try {
                    if (!((line = reader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(line);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
