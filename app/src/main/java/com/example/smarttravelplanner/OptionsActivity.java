package com.example.smarttravelplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OptionsActivity extends AppCompatActivity {

    private ListView optionsListView;
    private String[] options = {"Weather", "Population", "Crime Rate", "Best Places to Visit"};
    private String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // 1. Toolbar setup for Back Arrow
        Toolbar toolbar = findViewById(R.id.optionsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("City Options");
        }

        // 2. City Name receive karna
        selectedCity = getIntent().getStringExtra("city_name");

        TextView titleText = findViewById(R.id.selectedCityText);
        titleText.setText("Exploring " + selectedCity);

        // 3. ListView Setup
        optionsListView = findViewById(R.id.optionsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, options);
        optionsListView.setAdapter(adapter);

        // 4. Click Listener Logic
        optionsListView.setOnItemClickListener((parent, view, position, id) -> {
            String option = options[position];
            Intent intent = null; // Intent initialize kiya

            if (option.equals("Weather")) {
                intent = new Intent(OptionsActivity.this, WeatherActivity.class);
            } else if (option.equals("Population")) {
                intent = new Intent(OptionsActivity.this, PopulationActivity.class);
            } else if (option.equals("Crime Rate")) {
                intent = new Intent(OptionsActivity.this, CrimeRateActivity.class);
            } else if (option.equals("Best Places to Visit")) {
                // --- SQLite Logic Link ---

                intent = new Intent(OptionsActivity.this, PlacesActivity.class);
            }

            if (intent != null) {
                // City name ko agli screen par pass karna
                intent.putExtra("city_name", selectedCity);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}