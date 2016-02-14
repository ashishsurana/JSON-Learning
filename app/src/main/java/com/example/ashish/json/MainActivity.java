package com.example.ashish.json;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WORKING ","YES");
                new ReadPlacesFeedTask().execute("http://api.geonames.org/postalCodeSearchJSON?postalcode=311021&maxRows=10&username=demo");
            }
        });
    }
    public String readJSONFeed(String URL){
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient= new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("WORKING","inside 200");
            if (statusCode==200){
                Log.d("WORKING","inside 200");
                HttpEntity entity = response.getEntity();
                InputStream inputStream  =  entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line ;
                while ((line = reader.readLine())!=null){
                    stringBuilder.append(line);
                }
                inputStream.close();
            }
            else {
                Log.d("JSON","Failed to Download");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }



    private  class ReadPlacesFeedTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String temp = readJSONFeed(params[0]);
            return temp;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                Log.d("WORKING ","YES In postexe");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray postalcodeitems = new JSONArray(jsonObject.getString("postalCodes"));
                Log.d("RESULT",postalcodeitems.toString());

                for (int i=0; i<postalcodeitems.length(); i++){
                    JSONObject postalCodesItem = postalcodeitems.getJSONObject(i);
                    Log.d("RESULT1",postalCodesItem.getString("placeName"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
