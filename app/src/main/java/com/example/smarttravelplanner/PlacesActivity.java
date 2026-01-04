package com.example.smarttravelplanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;
    private String selectedCity;
    private ArrayList<PlaceModel> placesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Toolbar toolbar = findViewById(R.id.placesToolbar);
        setSupportActionBar(toolbar);
        selectedCity = getIntent().getStringExtra("city_name");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Top Places in " + selectedCity);
        }

        listView = findViewById(R.id.placesListView);
        dbHelper = new DatabaseHelper(this);

        // --- Logic: Pehle Offline check karo ---
        loadData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getPlacesByCity(selectedCity);
        if (cursor != null && cursor.getCount() > 0) {
            // Agar Database mein data hai (Offline Mode)
            while (cursor.moveToNext()) {
                // Column index 2: Name, 3: Description, 4: WebUrl
                placesList.add(new PlaceModel(cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            updateUI();
            Toast.makeText(this, "Showing Offline Saved Data", Toast.LENGTH_SHORT).show();
            cursor.close();
        } else {
            // Agar Database khali hai toh Internet se fetch karo
            new FetchPlacesTask().execute(selectedCity);
        }
    }

    private class FetchPlacesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String apiURL = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=tourist+attractions+in+" + city + "&format=json";
            try {
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                return result.toString();
            } catch (Exception e) { return null; }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray searchResults = jsonObject.getJSONObject("query").getJSONArray("search");

                    for (int i = 0; i < Math.min(searchResults.length(), 5); i++) {
                        JSONObject obj = searchResults.getJSONObject(i);
                        String title = obj.getString("title");
                        String snippet = obj.getString("snippet").replaceAll("\\<.*?\\>", "");
                        String link = "https://en.wikipedia.org/wiki/" + title.replace(" ", "_");

                        // SQLite mein Save karna
                        dbHelper.insertPlace(selectedCity, title, snippet, link, "");
                        placesList.add(new PlaceModel(title, snippet, link));
                    }
                    updateUI();
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void updateUI() {
        PlacesAdapter adapter = new PlacesAdapter(this, placesList);
        listView.setAdapter(adapter);

        // Click Listener: Nayi logic (Getter method ke sath)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            // wikiUrl ki jagah getWikiUrl() use kiya hai
            intent.putExtra("url", placesList.get(position).getWikiUrl());
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}