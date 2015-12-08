package com.example.rafael.jsonapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=Barrie,ca&appid=2de143494c0b295cca9337e1e96b00e0");

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Cannot interact with UI layer
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                String result = "";

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    result += ((char) data);
                    data = reader.read();
                }

                return  result;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }

        // It is called when doInBackGround is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                //JSONArray weather = jsonObject.getJSONArray("weather");

                //HashMap<String, Double> temp = jsonObject.get

                String weather = jsonObject.getString("weather");

                JSONArray jarray = new JSONArray(weather);


                for(int i = 0; i < jarray.length(); i++) {
                    JSONObject jpart = jarray.getJSONObject(i);

                    Log.i("Main: ", jpart.getString("main"));
                    Log.i("Description: ", jpart.getString("description"));
                }

                JSONObject temp = (JSONObject) jsonObject.get("main");
                String city = jsonObject.getString("name");
                Log.i("City: ", city);
                Log.i("Temp: : ", String.valueOf(Double.valueOf(temp.getString("temp"))/32));
                //Log.i("See: ", temp.getString("temp"));







                Log.i("String: ", String.valueOf(weather.length()));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
