package com.example.android.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.about));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final MaterialButton creditsButton = findViewById(R.id.icon_credits_button);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog creditsDialogBox;

                String textviewText = getResources().getString(R.string.iconDetails);
                WebView webView = new WebView(AboutActivity.this);
                webView.loadData(textviewText, "text/html", "utf-8");
                LinearLayout layout = new LinearLayout(AboutActivity.this);
                layout.addView(webView);
                layout.setPadding(15,15,15,15);
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setCancelable(true);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(layout);

                //finally creating the alert dialog and displaying it
                creditsDialogBox = builder.create();
                creditsDialogBox.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}