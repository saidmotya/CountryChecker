package com.gfx.countrychecker;

public interface OnCheckerListener {

    void onCheckerCountry(String country, boolean userFromGG);

    void onCheckerError(String error);

}
