package com.example.android.weather;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends Activity {

    Handler handler;
    boolean internetON, permissionGiven;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }

    private void onClickListeners() {
        Button allowButton = findViewById(R.id.allow_location_access_button);
        allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(SplashActivity.this)
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
        });

        Button internet = findViewById(R.id.internet_ON_button);
        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternet();
            }
        });
    }

    private void checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo == null) {
            Toast.makeText(this, "Turn ON internet", Toast.LENGTH_LONG).show();
        } else if (!netInfo.isConnected()) {
            TextView internet = findViewById(R.id.turn_ON_Internet);
            internet.setText("Internet not working. Try Again");
        } else {
            RelativeLayout internetLayout = findViewById(R.id.internet_off);
            internetLayout.setVisibility(View.GONE);
            internetON = true;
            launchActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClickListeners();
        permissionsCheck();
        checkInternet();
    }

    private void launchActivity() {
        if (internetON && permissionGiven) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

    private void permissionsCheck() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            RelativeLayout permissionsLayout = findViewById(R.id.permission_layout);
            permissionsLayout.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout permissionsLyout = findViewById(R.id.permission_layout);
            permissionsLyout.setVisibility(View.GONE);
            permissionGiven = true;
            launchActivity();
        }
    }
}