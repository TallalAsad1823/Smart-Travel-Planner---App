package com.example.smarttravelplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 1. Toolbar setup
        Toolbar toolbar = findViewById(R.id.webviewToolbar);
        setSupportActionBar(toolbar);

        // Data receive karna (URL aur Place Name)
        String url = getIntent().getStringExtra("url");
        // Humein PlacesActivity se place ka naam bhi bhejna hoga
        placeName = getIntent().getStringExtra("place_name");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(placeName != null ? placeName : "Details");
        }

        // 2. WebView setup
        webView = findViewById(R.id.webViewMain);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        // 3. Floating Action Button for Maps
        FloatingActionButton fab = findViewById(R.id.fabLocation);
        fab.setOnClickListener(view -> {
            // Google Maps Intent for Directions
            // 'daddr' ka matlab hai Destination Address
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(placeName));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Check karein ke phone mein Maps app hai ya nahi
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Agar Maps app nahi hai toh Browser mein kholen
                Uri browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(placeName));
                startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
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