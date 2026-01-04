package com.example.smarttravelplanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    TextView cityNameText, tempText, descriptionText;
    // FIXED: API Key ko quotes mein likhna lazmi hai
    String API_KEY = "5398bea945599e40e1594e9dbf654b15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // --- Toolbar with Back Arrow ---
        Toolbar toolbar = findViewById(R.id.weatherToolbar); // Make sure this ID is in XML
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Live Weather");
        }

        cityNameText = findViewById(R.id.cityNameText);
        tempText = findViewById(R.id.tempText);
        descriptionText = findViewById(R.id.descriptionText);

        String city = getIntent().getStringExtra("city_name");
        if (city != null) {
            cityNameText.setText(city);
            // Weather Fetch Karne ka function
            new FetchWeatherTask().execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + API_KEY);
        }
    }

    // Back button kaam karne ke liye
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }
                return result.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                descriptionText.setText("Network Error");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                String temp = jsonObject.getJSONObject("main").getString("temp");
                String desc = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                tempText.setText(Math.round(Double.parseDouble(temp)) + "°C");
                descriptionText.setText(desc.toUpperCase());
            } catch (Exception e) {
                descriptionText.setText("City not found");
            }
        }
    }
}