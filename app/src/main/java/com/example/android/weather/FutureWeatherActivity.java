package com.example.android.weather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;

public class FutureWeatherActivity extends AppCompatActivity {

    boolean[] expanded = new boolean[]{
            true,
            false,
            false,
            false,
            false,
            false,
            false
    };
    private ListView futureDaysList;
    private ForecastWeatherActivityListAdapter forecastWeatherActivityListAdapter;
    private ArrayList<WeatherDataClass> weather;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_weather);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.sevenDayForecast));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        displayAd();
        getSavedWeatherData();
        displayList();
        onClickListeners();
    }

    private void displayAd() {
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void getSavedWeatherData() {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("weather", "");
        weather = gson.fromJson(json, new TypeToken<ArrayList<WeatherDataClass>>() {
        }.getType());

    }

    private void displayList() {

        TextView weekSummaryTextView = findViewById(R.id.week_summary);
        weekSummaryTextView.setText(weather.get(0).getWeekSummary());

        futureDaysList = (ListView) findViewById(R.id.forecast_list);

        ArrayList<WeatherDataClass> weatherList = new ArrayList<>();
        weatherList.add(weather.get(1));
        weatherList.add(weather.get(2));
        weatherList.add(weather.get(3));
        weatherList.add(weather.get(4));
        weatherList.add(weather.get(5));
        weatherList.add(weather.get(6));
        weatherList.add(weather.get(7));

        forecastWeatherActivityListAdapter = new ForecastWeatherActivityListAdapter(this, weatherList);
        futureDaysList.setAdapter(forecastWeatherActivityListAdapter);
    }

    private void onClickListeners() {
        futureDaysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!expanded[i]){
                    ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.testLayout);
                    layout.setVisibility(View.VISIBLE);
                    expanded[i]=true;
                }
                else{
                    ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.testLayout);
                    layout.setVisibility(View.GONE);
                    forecastWeatherActivityListAdapter.notifyDataSetChanged();
                    Arrays.fill(expanded,false);
                }
                forecastWeatherActivityListAdapter.notifyDataSetChanged();
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
