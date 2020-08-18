package com.example.android.weather;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    protected double latitude, longitude;
    private ArrayList<WeatherDataClass> weather = new ArrayList<>();
    private TextView subDetailTitle1, subDetailTitle2, subDetailTitle3;
    private TextView subDetailValue1, subDetailValue2, subDetailValue3;
    private ImageView subDetailIcon1, subDetailIcon2, subDetailIcon3;
    private TextView temperature, summary, location, highLow;
    private ImageView poweredByDarkSky;
    private TextView moreDetails;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ArrayList<SubDetailCardClass> subDetailsCard;
    private ArrayList<HourlyDetailsCardClass> hourlyDetailsCard;
    private String tempUnit;
    private String speedUnit;
    private String distanceUnit;
    private String[] subDetails;
    private ListView futureDaysList;
    private AdView mainAdView;
    private String weatherApiKey = "apikey";
    private AlertDialog loadingDialogBox;
    private int AUTOCOMPLETE_REQUEST_CODE;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FusedLocationProviderClient fusedLocationClient;
    String searchedLocationName;
    Boolean searchedLocation = false;

    static {
        System.loadLibrary("keys");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIDs();
        onClickListeners();
        checkLocationPermission();
        createToolbar();
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.inflateMenu(R.menu.toolbar_menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else if (item.getItemId() == R.id.search) {
            AUTOCOMPLETE_REQUEST_CODE = 1;
            List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields).setTypeFilter(TypeFilter.CITIES)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                final LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    searchedLocation = true;
                    searchedLocationName = place.getName();
                    fetchData();
                }
//                Toast.makeText(MainActivity.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
                Log.i("PLACES_RESULT_OK", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("PLACES_ERROR", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void displayAds(AdView adview) {

        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

        adview.setAdListener(new AdListener() {
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

    private void settingsValues() {

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(this);
        tempUnit = sharedPref.getString("temp_units", "1");
        speedUnit = sharedPref.getString("speed_units", "1");
        Set<String> sd = sharedPref.getStringSet("sub_details", null);
        subDetails = sd.toArray(new String[]{});
        settingUnits();
    }

    private void displayPreviousValues() {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("weather", "");
        weather = gson.fromJson(json, new TypeToken<ArrayList<WeatherDataClass>>() {
        }.getType());

        if (weather != null) {

            temperature.setText(weather.get(0).getCurrentTemperture(tempUnit) + tempUnit);
            summary.setText(weather.get(0).getDescription());
            location.setText(weather.get(0).getCityName());

            highLow.setText(weather.get(0).getHighTemperature(tempUnit) + tempUnit + " / " + weather.get(0).getLowTemperature(tempUnit) + tempUnit);

            displayMainSubDetails();

            displayFutureDayWeatherList();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (location.getText().equals("")) {
            internetService();
        }
        settingsValues();
        displayPreviousValues();
        displayHourlyDetails();
        displayMainSubDetails();
        displayAds(mainAdView);
        setActivityBackgroundColor();
    }

    public void setActivityBackgroundColor() {
        ConstraintLayout mainDetailsBk = findViewById(R.id.main_details);
        if (weather == null) {
            mainDetailsBk.setBackgroundResource(R.color.colorPrimaryDark);
            return;
        }
        if (weather.get(0).getIconName() == null) {
            mainDetailsBk.setBackgroundResource(R.color.colorPrimaryDark);
            return;
        }
        switch (weather.get(0).getIconName()) {
            case "clear-day":
                mainDetailsBk.setBackgroundResource(R.drawable.sunny_background);
                break;
            case "clear-night":
                mainDetailsBk.setBackgroundResource(R.drawable.night_background);
                break;
            case "rain":
                mainDetailsBk.setBackgroundResource(R.drawable.rain_background);
                break;
            case "snow":
                mainDetailsBk.setBackgroundResource(R.drawable.snow_background);
                break;
            case "sleet":
                mainDetailsBk.setBackgroundResource(R.drawable.sleet_background);
                break;
            case "wind":
                mainDetailsBk.setBackgroundResource(R.drawable.wind_background);
                break;
            case "fog":
                mainDetailsBk.setBackgroundResource(R.drawable.fog_background);
                break;
            case "cloudy":
                mainDetailsBk.setBackgroundResource(R.drawable.overcast_background);
                break;
            case "partly-cloudy-day":
                mainDetailsBk.setBackgroundResource(R.drawable.partly_cloudy_day_background);
                break;
            case "partly-cloudy-night":
                mainDetailsBk.setBackgroundResource(R.drawable.partly_cloudy_night_background);
                break;
            default:
                break;

        }
    }

    private void displayMainSubDetails() {
        if (weather == null) {
            return;
        }
        String[] arr = getResources().getStringArray(R.array.sub_details);
        for (int i = 0; i < 3; i++) {
            int x = Integer.valueOf(subDetails[i]);
            String title = arr[x - 1];
            if (i == 0) {
                subDetailTitle1.setText(title);
                subDetailValue1.setText(getValue(title));
                subDetailIcon1.setImageResource(getIconForDetails(title));
            } else if (i == 1) {
                subDetailTitle2.setText(title);
                subDetailValue2.setText(getValue(title));
                subDetailIcon2.setImageResource(getIconForDetails(title));
            } else if (i == 2) {
                subDetailTitle3.setText(title);
                subDetailValue3.setText(getValue(title));
                subDetailIcon3.setImageResource(getIconForDetails(title));
            }
        }
    }

    private int getIconForDetails(String title) {
        int icon;
        if (weather == null) {
            icon = R.drawable.clear_weather_icon;
            return icon;
        }
        switch (title) {
            case "Precipitation":
                if (weather.get(0).getPrecipitationType().equals("snow")) {
                    icon = R.drawable.snow_icon;
                } else {
                    icon = R.drawable.precipitation;
                }
                break;
            case "Real Feel":
                icon = R.drawable.real_feel_icon;
                break;
            case "Cloudiness":
                icon = R.drawable.cloudiness_icon;
                break;
            case "Wind":
                icon = R.drawable.wind_speed;
                break;
            case "Humidity":
                icon = R.drawable.humidity_icon;
                break;
            case "UV Index":
                icon = R.drawable.uv_index;
                break;
            case "Pressure":
                icon = R.drawable.pressure_icon;
                break;
            case "Visibility":
                icon = R.drawable.visibility;
                break;
            case "Sunrise Time":
                icon = R.drawable.sunrise;
                break;
            case "Sunset Time":
                icon = R.drawable.sunset;
                break;
            case "Low Temp Time":
                icon = R.drawable.lowtemp_icon;
                break;
            case "High Temp Time":
                icon = R.drawable.hightemp_icon;
                break;
            default:
                icon = R.drawable.clear_weather_icon;
                break;
        }
        return icon;
    }

    private String getValue(String title) {
        String value;
        if (weather == null) {
            value = " ";
            return value;
        }
        switch (title) {
            case "Precipitation":
                value = weather.get(0).getPrecipitation() + "%";
                break;
            case "Real Feel":
                value = weather.get(0).getApparentTemperature(tempUnit) + tempUnit;
                break;
            case "Cloudiness":
                value = weather.get(0).getCloudiness() + "%";
                break;
            case "Wind":
                value = weather.get(0).getWindSpeed(speedUnit) + speedUnit;
                break;
            case "Humidity":
                value = weather.get(0).getHumidity() + "%";
                break;
            case "UV Index":
                value = String.valueOf(weather.get(0).getUVindex());
                break;
            case "Pressure":
                value = weather.get(0).getPressure() + "hPa";
                break;
            case "Visibility":
                value = weather.get(0).getVisibility(distanceUnit) + distanceUnit;
                break;
            case "Sunrise Time":
                value = weather.get(0).getSunRiseTime();
                break;
            case "Sunset Time":
                value = weather.get(0).getSunSetTime();
                break;
            case "Low Temp Time":
                value = weather.get(0).getLowTemperatureTime();
                break;
            case "High Temp Time":
                value = weather.get(0).getHighTemperatureTime();
                break;
            default:
                value = "ERROR";
                break;
        }
        return value;
    }

    public void settingUnits() {
        if (tempUnit.equals("2")) {
            tempUnit = "°F";
        } else {
            tempUnit = "°C";
        }
        switch (speedUnit) {
            case "3":
                speedUnit = "m/s";
                distanceUnit = "km";
                break;
            case "2":
                speedUnit = "mi/hr";
                distanceUnit = "mi";
                break;
            default:
                speedUnit = "km/hr";
                distanceUnit = "km";
                break;
        }
    }

    @Override
    protected void onPause() {
        if (weather != null) {
            saveCurrentData();
        }
        super.onPause();
    }

    private void saveCurrentData() {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(weather);
        prefsEditor.putString("weather", json);
        prefsEditor.commit();
    }

    public void checkLocationPermission() {

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permForLoc)
                        .setMessage(R.string.messForLoc)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                                onBackPressed();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void onClickListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                internetService();
            }
        });

        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weather != null) {
                    showSubDetailsDialogBox();
                } else {
                    Toast.makeText(MainActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        futureDaysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2) {
                    Intent myIntent = new Intent(MainActivity.this, FutureWeatherActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }
            }
        });

        poweredByDarkSky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://darksky.net/poweredby/";

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(url));

                startActivity(intent);
            }
        });
    }

    private void displayHourlyDetails() {

        if (weather == null) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        hourlyDetailsCard = new ArrayList<>();

        addHourlyDataToCard();

        HourlyDetailsAdapter adapter = new HourlyDetailsAdapter(hourlyDetailsCard);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addHourlyDataToCard() {

        TextView hourlySummary = findViewById(R.id.hourly_summary);
        hourlySummary.setText(weather.get(0).getHourlySummary());

        String timezone = weather.get(0).getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("h a");
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));

        hourlyDetailsCard = new ArrayList<>();
        for (int i = 0; i < weather.get(0).getHourlyTempArray().size(); i++) {
            String temp;
            if (tempUnit.equals("°F")) {
                temp = (weather.get(0).getHourlyTempArray().get(i) * 9 / 5) + 32 + "°F";
            } else {
                temp = weather.get(0).getHourlyTempArray().get(i) + "°C";
            }
            String time = sdf.format(weather.get(0).getHourlyTimeArray().get(i) * 1000);
            int icon = weather.get(0).getIconForWeather(weather.get(0).getHourlyIconArray().get(i));
            int precip = (weather.get(0).getHourlyPrecipitationArray().get(i));

            HourlyDetailsCardClass a = new HourlyDetailsCardClass(time, temp, icon, precip);
            hourlyDetailsCard.add(a);
        }
    }

    private void showSubDetailsDialogBox() {

        //We will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.sub_details_dialog_box, null);


        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        subDetailsCard = new ArrayList<>();
        addDataToCard();
        SubDetailsAdapter adapter = new SubDetailsAdapter(subDetailsCard);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addDataToCard() {
        int precipitation_icon = R.drawable.precipitation;
        if (weather.get(0).getPrecipitationType().equals("snow")) {
            precipitation_icon = R.drawable.snow_icon;
        }

        int[] icon = new int[]{
                precipitation_icon,
                R.drawable.cloudiness_icon,
                R.drawable.wind_speed,
                R.drawable.humidity_icon,
                R.drawable.uv_index,
                R.drawable.real_feel_icon,
                R.drawable.pressure_icon,
                R.drawable.visibility,
                R.drawable.sunrise,
                R.drawable.sunset,
                R.drawable.lowtemp_icon,
                R.drawable.hightemp_icon
        };
        String[] title = new String[]{
                "Precipitation",
                "Cloudiness",
                "Wind",
                "Humidity",
                "UV Index",
                "Real Feel",
                "Pressure",
                "Visibility",
                "Sunrise Time",
                "Sunset Time",
                "Low Temp Time",
                "High Temp Time"
        };
        String[] value = new String[]{
                weather.get(0).getPrecipitation() + "%",
                weather.get(0).getCloudiness() + "%",
                weather.get(0).getWindSpeed(speedUnit) + speedUnit,
                weather.get(0).getHumidity() + "%",
                String.valueOf(weather.get(0).getUVindex()),
                weather.get(0).getApparentTemperature(tempUnit) + tempUnit,
                weather.get(0).getPressure() + "hPa",
                weather.get(0).getVisibility(distanceUnit) + distanceUnit,
                String.valueOf(weather.get(0).getSunRiseTime()),
                String.valueOf(weather.get(0).getSunSetTime()),
                String.valueOf(weather.get(0).getLowTemperatureTime()),
                String.valueOf(weather.get(0).getHighTemperatureTime())
        };

        for (int i = 0; i < title.length; i++) {
            SubDetailCardClass a = new SubDetailCardClass(title[i], value[i], icon[i]);
            subDetailsCard.add(a);
        }
    }

    private void displayFutureDayWeatherList() {

        futureDaysList = (ListView) findViewById(R.id.future_days_list);

        ArrayList<WeatherDataClass> weatherList = new ArrayList<>();
        weatherList.add(weather.get(1));
        weatherList.add(weather.get(2));

        FutureDaysListAdapter futureDaysListAdapter = new FutureDaysListAdapter(this, weatherList);
        futureDaysList.setAdapter(futureDaysListAdapter);

    }

    private void setIDs() {

        MobileAds.initialize(this, "adskey");

        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(),key);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);


        mainAdView = findViewById(R.id.adView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        futureDaysList = findViewById(R.id.future_days_list);
        ImageView more = new ImageView(getBaseContext());
        more.setImageResource(R.drawable.arrow_down_icon);
        more.setPadding(0, 16, 0, 16);
        futureDaysList.addFooterView(more);

        temperature = findViewById(R.id.main_details_temp);
        summary = findViewById(R.id.main_details_summary);
        location = findViewById(R.id.location_text);
        highLow = findViewById(R.id.main_details_high_low);

        subDetailTitle1 = findViewById(R.id.detail_1_title);
        subDetailTitle2 = findViewById(R.id.detail_2_title);
        subDetailTitle3 = findViewById(R.id.detail_3_title);
        subDetailValue1 = findViewById(R.id.detail_1_value);
        subDetailValue2 = findViewById(R.id.detail_2_value);
        subDetailValue3 = findViewById(R.id.detail_3_value);
        subDetailIcon1 = findViewById(R.id.detail_1_icon);
        subDetailIcon2 = findViewById(R.id.detail_2_icon);
        subDetailIcon3 = findViewById(R.id.detail_3_icon);

        moreDetails = findViewById(R.id.more_details);
        poweredByDarkSky = findViewById(R.id.poweredby_darksky);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void internetService() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo == null) {
            Toast.makeText(this, "Turn on Internet!!!", Toast.LENGTH_LONG).show();
        } else if (!netInfo.isConnected()) {
            Toast.makeText(this, "Internet not working!!!", Toast.LENGTH_LONG).show();
        } else {
            ProgressBar progressBar = new ProgressBar(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.addView(progressBar);
            layout.setGravity(Gravity.CENTER);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            //setting the view of the builder to our custom view that we already inflated
            builder.setView(layout);

            //finally creating the alert dialog and displaying it
            loadingDialogBox = builder.create();
            loadingDialogBox.setCancelable(true);
            loadingDialogBox.setTitle("Fetching...");
            loadingDialogBox.setMessage("Location Data");
            loadingDialogBox.show();

            getLastKnownLocation();
//            getLocation();
            /*if (latitude == 0 && longitude == 0) {
                Toast.makeText(MainActivity.this, "Location returns null", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    private void getLastKnownLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        loadingDialogBox.dismiss();
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            DecimalFormat df = new DecimalFormat("#.00");
                            double xLatitude = Double.valueOf(df.format(latitude));
                            double xLongitude = Double.valueOf(df.format(longitude));

                            if (weather == null) {
                                fetchData();
                            } else {
                                long timeDif = getCurrentTime() - weather.get(0).getTime();
                                if (timeDif > 1800) {
                                    fetchData();
                                } else if (xLatitude != weather.get(0).getLatitude() || xLongitude != weather.get(0).getLongitude()) {
                                    fetchData();
                                }
                            }
                            latitude = xLatitude;
                            longitude = xLongitude;
//                            Toast.makeText(MainActivity.this,"Location is :"+latitude+","+longitude, Toast.LENGTH_SHORT).show();
                            // Logic to handle location object
                        } else {
                            Toast.makeText(MainActivity.this, "Could not fetch location!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchData() {

        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask();
        String key = weatherApiKey;
//        String key = new String(Base64.decode(getNativeKey1(),Base64.DEFAULT), StandardCharsets.UTF_8);
        String WEATHER_REQUEST_URL = "https://api.darksky.net/forecast/" + key + "/";
//        Toast.makeText(this, WEATHER_REQUEST_URL, Toast.LENGTH_LONG).show();
        weatherAsyncTask.execute(WEATHER_REQUEST_URL + latitude + "," + longitude + "?exclude=flags&units=si");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    internetService();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Important Info:")
                            .setMessage("Go to app permissions and enable location")
                            .setPositiveButton("Give Permission", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:" + MainActivity.this.getPackageName()));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MainActivity.this.startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }

    private void getLocation() {
        LocationProvider.requestSingleUpdate(this,
                new LocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(LocationProvider.GPSCoordinates location) {
//                        Toast.makeText(MainActivity.this, "Location is: " + location.latitude + "," + location.longitude, Toast.LENGTH_SHORT).show();
                        loadingDialogBox.dismiss();
                        latitude = location.latitude;
                        longitude = location.longitude;
                        DecimalFormat df = new DecimalFormat("#.00");
                        double xLatitude = Double.valueOf(df.format(latitude));
                        double xLongitude = Double.valueOf(df.format(longitude));

                        if (weather == null) {
                            fetchData();
                        } else {
                            long timeDif = getCurrentTime() - weather.get(0).getTime();
                            if (timeDif > 1800) {
                                fetchData();
                            } else if (xLatitude != weather.get(0).getLatitude() || xLongitude != weather.get(0).getLongitude()) {
                                fetchData();
                            }
                        }
                        latitude = xLatitude;
                        longitude = xLongitude;
                    }
                });
    }

    private void setCityName() {
        if (searchedLocation) {
            location.setText(searchedLocationName);
            weather.get(0).setCityName(searchedLocationName);
            searchedLocation = false;
            return;
        }
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() != 0) {
                String loc = addresses.get(0).getLocality();
                if (loc == null) {
                    loc = addresses.get(0).getSubAdminArea();
                }
                location.setText(loc);
                weather.get(0).setCityName(location.getText().toString());
            } else {
                location.setText("No Location");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        Date cals = Calendar.getInstance(Locale.getDefault()).getTime();
        long milliseconds = cals.getTime();
        long currTime = milliseconds / 1000L;
        return currTime;
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<WeatherDataClass>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = new ProgressBar(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.addView(progressBar);
            layout.setGravity(Gravity.CENTER);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            //setting the view of the builder to our custom view that we already inflated
            builder.setView(layout);

            //finally creating the alert dialog and displaying it
            loadingDialogBox = builder.create();
            loadingDialogBox.setTitle("Fetching...");
            loadingDialogBox.setMessage("Weather Data");
            loadingDialogBox.show();
        }

        @Override
        protected ArrayList<WeatherDataClass> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return weather;
            }

            ArrayList<WeatherDataClass> result = WeatherDataQuery.fetchWeatherData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherDataClass> data) {

            weather = data;
            if (data == null) {
                Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
            } else {

                setCityName();

                temperature.setText(data.get(0).getCurrentTemperture(tempUnit) + tempUnit);
                summary.setText((data.get(0).getSummary()));
                highLow.setText(data.get(0).getHighTemperature(tempUnit) + tempUnit + " / " + data.get(0).getLowTemperature(tempUnit) + tempUnit);

                displayMainSubDetails();
                displayHourlyDetails();
                displayFutureDayWeatherList();
                setActivityBackgroundColor();

                loadingDialogBox.dismiss();
            }
        }
    }

}
