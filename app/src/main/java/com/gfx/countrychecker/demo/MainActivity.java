package com.gfx.countrychecker.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.gfx.countrychecker.CheckerType;
import com.gfx.countrychecker.CountryChecker;
import com.gfx.countrychecker.OnCheckerListener;

public class MainActivity extends AppCompatActivity {

    protected CountryChecker countryChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize views for demo
        TextView location_text = findViewById(R.id.location);
        TextView ggStore_text = findViewById(R.id.ggStore);

        //Initialize CountryChecker library :
        countryChecker = new CountryChecker(this, CheckerType.SimCountryIso);
        countryChecker.setOnCheckerListener(new OnCheckerListener() {
            @Override
            public void onCheckerCountry(String country, boolean userFromGG) {
                //get user country & check if the user install your app from GG play store.
                location_text.setText(country);

                if (userFromGG){
                    ggStore_text.setText("Yes");
                }else {
                    ggStore_text.setText("No");
                }


            }

            @Override
            public void onCheckerError(String error) {
                //failed to get user country.
                location_text.setText(error);

                //you can show No Connection dialog her ! and get retry again to get info.

            }
        });

    }
}