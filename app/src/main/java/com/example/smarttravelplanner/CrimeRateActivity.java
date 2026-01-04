package com.example.smarttravelplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CrimeRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_rate);

        Toolbar toolbar = findViewById(R.id.crimeToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Safety Index");
        }

        TextView cityTitle = findViewById(R.id.crimeCityName);
        TextView crimeStatus = findViewById(R.id.crimeStatus);

        String city = getIntent().getStringExtra("city_name");
        cityTitle.setText(city);


        crimeStatus.setText("Low to Moderate"); // Default status
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