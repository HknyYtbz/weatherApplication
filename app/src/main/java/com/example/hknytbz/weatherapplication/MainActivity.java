package com.example.hknytbz.weatherapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    final String endPoint = "http://api.openweathermap.org/data/2.5/weather?q=";
    final String apiKey =  "&appid=916afff77b79c8e5ecb571ae3fb4bc52";
    EditText enteredCity;
    Button selectButton;
    public class GetJSON extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {

            try {

                String result = "";
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader inRead = new InputStreamReader(in);
                int data = inRead.read();
                while(data != -1)
                {
                    result += (char) data;
                    data = inRead.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject json = new JSONObject(result);
                JSONArray weatherArray = json.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                TextView cityName = (TextView) findViewById(R.id.textView9);
                TextView weatherInf = (TextView) findViewById(R.id.textView10);
                TextView detail = (TextView) findViewById(R.id.textView11);
                TextView temperature = (TextView) findViewById(R.id.textView3);
                cityName.setText(json.getString("name"));
                weatherInf.setText(weather.getString("main"));
                String temp = weather.getString("description").substring(0,1).toUpperCase() + weather.getString("description").substring(1);
                detail.setText(temp);
                String temper = json.getJSONObject("main").getString("temp");
                double temperr = Double.parseDouble(temper);

                temperr = (temperr-273.0);
                temperature.setText(new Integer((int)temperr).toString()+" °C");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectButton= (Button) findViewById(R.id.button);
        enteredCity= (EditText) findViewById(R.id.editText);


            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(enteredCity.getWindowToken(),0);


                    try {
                        GetJSON getJSON = new GetJSON();
                        String city = URLEncoder.encode(enteredCity.getText().toString(),"UTF-8");
                        if(!city.equals("")) {

                            String requestedURL = endPoint + city + apiKey;
                            getJSON.execute(requestedURL).get();
                        }
                        else
                            throw new Exception();
                    } catch (Exception e) {
                        Log.i("Context",getApplicationContext().toString());
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Could not find the weather!",Toast.LENGTH_LONG);
                    }


                }
            });

    }
}
