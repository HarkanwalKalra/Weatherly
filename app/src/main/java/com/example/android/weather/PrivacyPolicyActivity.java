package com.example.android.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ImageButton closeButton =  findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        WebView privacyPolicy = findViewById(R.id.privacyPolicy);
        String htmlAsString = getString(R.string.PrivacyPolicyText);

        privacyPolicy.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

    }
}
