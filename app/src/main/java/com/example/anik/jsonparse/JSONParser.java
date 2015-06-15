package com.example.anik.jsonparse;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    private static final String LOG_TAG = "JSONParser";
    private static InputStream inputStream = null;
    private static JSONArray jsonArray = null;
    private String json = "";

    public JSONParser(){

    }

    public JSONArray getJsonArray(String url){
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try{
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity httpEntity = response.getEntity();
                InputStream content = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
            } else{
                Log.v(LOG_TAG, "Failed to load from server");
            }
        } catch(ClientProtocolException e){
            Log.v(LOG_TAG, e.toString());
        } catch (IOException e){
            Log.v(LOG_TAG, e.toString());
        }

        try{
            jsonArray = new JSONArray(stringBuilder.toString());
        } catch(JSONException e){
            Log.v(LOG_TAG, "Error parsing data: " + e.toString());
        }

        return jsonArray;
    }
}
