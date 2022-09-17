package com.gfx.countrychecker;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SAID MOTYA on 09/07/2022.
 * Github : https://github.com/saidmotya
 * Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 * *******************************************************************************
 * Use CountryChecker to get country user with two methode (Server or via Sim ISO )
 * and also can you check if the user install your app from google play store or from another platform.
 * *******************************************************************************
 */

public class CountryChecker {

    private final Activity activity;
    private OnCheckerListener onCheckerListener;

    private final List<String> validate = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));
    private final String serverConfig = "https://www.speedtest.net/speedtest-config.php";

    /**
     * CountryChecker :
     * @Activity activity
     * @CheckerType checkerType : SpeedServer (need connection) or SimCountryIso (without connection)
     */
    public CountryChecker(Activity activity, CheckerType checkerType) {
        this.activity = activity;
        switch (checkerType) {
            case SpeedServer:
                setServerInfo();
                break;
            case SimCountryIso:
                setIsoInfo();
                break;
        }

    }

    /**
     * setServerInfo
     * Make connection to server https://www.speedtest.net/speedtest-config.php
     * and get value of country from line <client ip> country="MA" </client>
     */
    private void setServerInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverConfig);
                    InputStream stream = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("<client ip")) {
                            String country = line.split("country=\"")[1].split("\"")[0].toLowerCase();
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (onCheckerListener != null) {
                                        onCheckerListener.onCheckerCountry(country, isUserFromGG());
                                    }
                                }
                            });

                            break;
                        }
                    }
                    stream.close();
                    reader.close();
                } catch (Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (onCheckerListener != null) {
                                onCheckerListener.onCheckerCountry(null, isUserFromGG());
                                onCheckerListener.onCheckerError(e.getMessage());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * setIsoInfo
     * get the SIM country ISO via TelephonyManager
     */
    private void setIsoInfo() {
        try {

            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            //give same time to skip onCheckerListener null.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (onCheckerListener != null) {
                        onCheckerListener.onCheckerCountry(telephonyManager.getSimCountryIso(), isUserFromGG());
                    }
                }
            }, 100);


        } catch (Exception e) {
            if (onCheckerListener != null) {
                onCheckerListener.onCheckerError(e.getMessage());
            }
        }
    }

    /**
     * isUserFromGG
     * check if the user install our app from google play store
     */
    private boolean isUserFromGG() {

        String installerManager;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                installerManager = activity.getPackageManager().getInstallSourceInfo(activity.getPackageName()).getInstallingPackageName();
            } else {
                installerManager = activity.getPackageManager().getInstallerPackageName(activity.getPackageName());
            }
            return installerManager != null && validate.contains(installerManager);


        } catch (Exception e) {
            return false;
        }

    }

    public void setOnCheckerListener(OnCheckerListener onCheckerListener) {
        this.onCheckerListener = onCheckerListener;
    }
}
