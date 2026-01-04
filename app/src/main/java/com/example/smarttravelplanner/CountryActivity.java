package com.example.smarttravelplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.Arrays;

public class CountryActivity extends AppCompatActivity {

    private ListView countryListView;
    private EditText searchCountry;
    private ArrayAdapter<String> adapter;

    String[] countriesArray = {
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Australia", "Austria",
            "Bahamas", "Bahrain", "Bangladesh", "Belgium", "Brazil", "Canada", "China", "Colombia",
            "Denmark", "Egypt", "Finland", "France", "Germany", "Greece", "Iceland", "India", "Indonesia",
            "Iran", "Iraq", "Ireland", "Italy", "Japan", "Jordan", "Kuwait", "Malaysia", "Mexico",
            "Morocco", "Netherlands", "New Zealand", "Norway", "Oman", "Pakistan", "Palestine", "Portugal",
            "Qatar", "Russia", "Saudi Arabia", "Singapore", "South Africa", "Spain", "Sri Lanka", "Sweden",
            "Switzerland", "Thailand", "Turkey", "United Arab Emirates", "United Kingdom", "United States", "Vietnam"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- 1. THEME MANAGEMENT (Requirement 1.4) ---
        SharedPreferences themePref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int themeMode = themePref.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        // --- TOOLBAR SETUP ---
        Toolbar toolbar = findViewById(R.id.countryToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Country");
        }

        countryListView = findViewById(R.id.countryListView);
        searchCountry = findViewById(R.id.searchCountry);

        ArrayList<String> countryList = new ArrayList<>(Arrays.asList(countriesArray));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryList);
        countryListView.setAdapter(adapter);

        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        countryListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCountry = adapter.getItem(position);
            Intent intent = new Intent(CountryActivity.this, CityActivity.class);
            intent.putExtra("country_name", selectedCountry);
            startActivity(intent);
        });
    }

    // --- UPDATED OPTIONS MENU (Requirement 1.2 & 6.1) ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item1 = menu.add(0, 1, 0, "Light Mode");
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item2 = menu.add(0, 2, 0, "Dark Mode");
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (item.getItemId() == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        } else if (item.getItemId() == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_YES);
        }

        editor.apply();
        return super.onOptionsItemSelected(item);
    }
}