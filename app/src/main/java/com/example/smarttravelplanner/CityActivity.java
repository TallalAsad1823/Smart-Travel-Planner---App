package com.example.smarttravelplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    ListView cityListView;
    TextView countryTitle;
    EditText searchCity;
    ArrayAdapter<String> adapter;
    ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        // --- BACK ARROW SETUP ---
        Toolbar toolbar = findViewById(R.id.cityToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Select City");
        }

        cityListView = findViewById(R.id.cityListView);
        countryTitle = findViewById(R.id.countryTitle);
        searchCity = findViewById(R.id.searchCity);

        String country = getIntent().getStringExtra("country_name");
        if (country == null) country = "Pakistan";

        countryTitle.setText("Cities in " + country);


        loadCitiesFromJson(country);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        cityListView.setAdapter(adapter);

        searchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        cityListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = adapter.getItem(position);
            Intent intent = new Intent(CityActivity.this, OptionsActivity.class);
            intent.putExtra("city_name", selectedCity);
            startActivity(intent);
        });
    }

    // Back Arrow Action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCitiesFromJson(String countryName) {
        try {
            InputStream is = getAssets().open("cities.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            cities.clear();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.trim().equalsIgnoreCase(countryName.trim())) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        cities.add(reader.nextString());
                    }
                    reader.endArray();
                    break;
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            reader.close();

            if (cities.isEmpty()) {
                cities.add("No cities found for " + countryName);
            }
        } catch (Exception e) {
            cities.add("Error loading data");
        }
    }
}