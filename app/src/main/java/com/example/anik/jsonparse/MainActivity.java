package com.example.anik.jsonparse;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    ListView listView;
    private Context context;
    private static String url = Link.CATEGORIES;
    ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        new ProgressTask(MainActivity.this).execute();
    }
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;
        private Activity activity;
        private Context context;

        public ProgressTask(Activity activity){
            this.activity = activity;
            this.context = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            JSONArray json = jsonParser.getJsonArray(url);
            for(int i = 0; i < json.length(); ++i){
                try{
                    JSONObject object = json.getJSONObject(i);
                    String category_id = object.getString("CategoryID");
                    String category_name = object.getString("CategoryName");
                    String category_description = object.getString("Description");

                    HashMap<String, String> map = new HashMap<>();
                    Log.v("TAGTAG", "id: " + category_id + " name: " + category_name + " description: " + category_description );
                    map.put("CategoryID", category_id);
                    map.put("CategoryName", category_name);
                    map.put("Description", category_description);
                    jsonList.add(map);

                } catch(JSONException e){
                    Log.v("Error", e.toString());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(this.dialog.isShowing()){
                this.dialog.dismiss();
            }

            String from[] = {
                    "CategoryID",
                    "CategoryName",
                    "Description"
            };
            int to[] = {
                    R.id.categoryId,
                    R.id.categoryName,
                    R.id.categoryDescription
            };

            ListAdapter adapter = new SimpleAdapter(context, jsonList, R.layout.layout_category, from, to);
            listView.setAdapter(adapter);
        }
    }
}
