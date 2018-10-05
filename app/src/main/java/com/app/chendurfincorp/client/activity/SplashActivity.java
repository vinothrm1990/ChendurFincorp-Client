package com.app.chendurfincorp.client.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class SplashActivity extends AppCompatActivity implements InternetConnectivityListener {

    private static int SPLASH_TIME_OUT = 1500;
    InternetAvailabilityChecker internetAvailabilityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);

        Constants.pref = getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        internetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (!isConnected) {
            Toasty.warning(SplashActivity.this, "Check your Network Connection", Toast.LENGTH_LONG, true).show();
        } else if (isConnected){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (Constants.pref.getBoolean("isLogged", false)) {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Bungee.shrink(SplashActivity.this);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Bungee.fade(SplashActivity.this);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
