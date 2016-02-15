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

import java.io.BufferedInputStream;
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
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("WORKING", "Button PRESSED");
//                new ReadPlacesFeedTask().execute("/http://api.geonames.org/postalCodeSearchJSON?postalcode=311021&maxRows=10&username=demo");
                new ReadPlacesFeedTask().execute("http://www.getpincode.info/api/pincode?q=delhi");
            }
        });

    }
//    public String readJSONFeed(String URL){
//        StringBuilder stringBuilder = new StringBuilder();
//        HttpClient httpClient= new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(URL);
//        try {
//            HttpResponse response = httpClient.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
////            Log.d("WORKING","inside 200");
//            if (statusCode==200){
////                Log.d("WORKING","inside 200");
//                HttpEntity entity = response.getEntity();
//                InputStream inputStream  =  entity.getContent();
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(inputStream));
//                String line ;
//                while ((line = reader.readLine())!=null){
//                    stringBuilder.append(line);
//                }
//                inputStream.close();
//            }
//            else {
//                Log.d("JSON","Failed to Download");
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//    }



    private  class ReadPlacesFeedTask extends AsyncTask<String, Void, String>{
        HttpURLConnection connection ;

        @Override
        protected String doInBackground(String... params) {
            Log.d("WORKING","Inside background");
            StringBuilder temp = new StringBuilder();
            try{
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                int status = connection.getResponseCode();
                if (status==200){
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    Log.d("WORKING ", "HOLY1");
                    if (reader.equals("")) {
                        Log.d("WORKING ", "HOLY");
                    }
                    String line ;

                    while ((line = reader.readLine()) != null) {
                        temp.append(line);
                        Log.d("WORKING ","YES ");
                    }
                }
                else{
                    Log.d("Status "," " + status);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("WORKING" , e.toString());
            } finally {
                connection.disconnect();
            }
                Log.d("JSONFEED",temp.toString());
            return temp.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                Log.d("WORKING ","YES In postexe");
                JSONObject jsonObject = new JSONObject(s);
                Log.d("FINAL",jsonObject.get("pincode").toString());
                
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
