
# CountryChecker [![](https://jitpack.io/v/saidmotya/CountryChecker.svg)](https://jitpack.io/#saidmotya/CountryChecker)

An Android library for member secretGFX group, Help developer to get user location country & check the app is insalled from GG Play Store.

## Installation

*Step 1.* Add the JitPack repository to your project `build.gradle` file

```gradle

allprojects {
    repositories {
        //your repository
        maven { url 'https://jitpack.io' }
    }
}

```

*Step 2.* Add the dependency in the form

```gradle

dependencies {
    implementation 'com.github.saidmotya:CountryChecker:1.0.0'
}

```

## Initialize

```java

 countryChecker = new CountryChecker(this, CheckerType.SpeedServer); //check country via server speedtest.
 countryChecker.setOnCheckerListener(new OnCheckerListener() {
            @Override
            public void onCheckerCountry(String country, boolean userFromGG) {
                //get user country & check if the user install your app from GG play store.
            }

            @Override
            public void onCheckerError(String error) {
                //failed to get user country.
                //you can show No Connection dialog her ! and get retry again to get info.
            }
        });

```

## CheckerType

| CheckerType | Description |
| --- | --- |
| `SpeedServer`   | Make connection to server https://www.speedtest.net/speedtest-config.php |
| `SimCountryIso` | Get the SIM country ISO via TelephonyManager |
