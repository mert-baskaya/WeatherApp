package com.example.watherapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watherapp.R;
import com.example.watherapp.api.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private double lon;
    private double lat;
    private int weatherId;
    private String main;
    private String description;
    private String icon;
    private String base;
    private long temp;
    private int pressure;
    private int humidity;
    private long temp_min;
    private long temp_max;
    private int visibility;
    private long speed;
    private int deg;
    private String all;
    private long dt;
    private int type;
    private int objectId;
    private long message;
    private String country;
    private long sunrise;
    private long sunset;
    private int id;
    private String name;
    private int response;

    private String apiKey = "0d25b32b86547a3a204d7dccf7f17657";

    private TextView cityNameTV;
    private TextView humidityTV;
    private TextView tempTV;
    private TextView pressureTV;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetWeatherInfo().execute();
        cityNameTV = findViewById(R.id.activity_main_city_textView);
        //humidityTV = findViewById(R.id.activity_main_humidity_textView);
        tempTV = findViewById(R.id.activity_main_temp_textView);
        //pressureTV = findViewById(R.id.activity_main_pressure_textView);
        refresh = findViewById(R.id.activity_main_refresh_button);




        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetWeatherInfo().execute();
            }
        });
    }

    private class GetWeatherInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Connecting to the weather server", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String url = "https://api.openweathermap.org/data/2.5/weather?q=Istanbul,tr&appid="+apiKey;
            String jsonStr = httpHandler.makeServiceCall(url);

            Log.e(TAG, "Response from URL" + jsonStr);

            try {
                JSONObject reader = new JSONObject(jsonStr);

                //coord object      "coord":{"lon":-0.13,"lat":51.51}
                JSONObject coord = reader.getJSONObject("coord");
                lon = coord.getDouble("lon");
                lat = coord.getDouble("lat");

                //weather array     "weather":[{"id":300,"main":"Drizzle","description":"light intensity drizzle","icon":"09d"}]
                JSONArray weatherArray = reader.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                weatherId = weather.getInt("id");
                main = weather.getString("main");
                description = weather.getString("description");
                icon = weather.getString("icon");

                //base value        "base":"stations"
                base = reader.getString("base");

                //main object       "main":{"temp":280.32,"pressure":1012,"humidity":81,"temp_min":279.15,"temp_max":281.15}
                JSONObject objectMain = reader.getJSONObject("main");
                temp = objectMain.getLong("temp");
                pressure = objectMain.getInt("pressure");
                humidity = objectMain.getInt("humidity");
                temp_min = objectMain.getLong("temp_min");
                temp_max = objectMain.getLong("temp_max");

                //visibility value  "visibility":10000
                visibility = reader.getInt("visibility");


                //wind object       "wind":{"speed":4.1,"deg":80}
                JSONObject wind = reader.getJSONObject("wind");
                speed = wind.getLong("speed");
                deg = wind.getInt("deg");

                //clouds object     "clouds":{"all":90}
                JSONObject clouds = reader.getJSONObject("clouds");
                all = clouds.getString("all");

                //dt value          "dt":1485789600
                dt = reader.getLong("dt");

                //sys object        "sys":{"type":1,"id":5091,"message":0.0103,"country":"GB","sunrise":1485762037,"sunset":1485794875}
                JSONObject sys = reader.getJSONObject("sys");
                type = sys.getInt("type");
                objectId = sys.getInt("id");
                message = sys.getLong("message");
                country = sys.getString("country");
                sunrise = sys.getLong("sunrise");
                sunset = sys.getLong("sunset");

                //id value          "id":2643743
                id = reader.getInt("id");

                //name value        "name":"London"
                name = reader.getString("name");

                //response code     "cod":200
                response = reader.getInt("cod");

            } catch (JSONException e) {
                Log.e(TAG,""+e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Updating resources", Toast.LENGTH_SHORT).show();
            cityNameTV.setText(name);
            //humidityTV.setText(humidity);
            tempTV.setText(kelvinToCelsius(temp));
            //pressureTV.setText(pressure);
        }
    }

    private String kelvinToCelsius(long kelvin){
        return (kelvin-273)+" °C";
    }
}
