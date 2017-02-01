package com.reactivemobile.ukpoliceapp.objects;

import com.google.gson.annotations.Expose;

/**
 * A single date for availability
 */
public class StreetLevelAvailabilityDate {
    @Expose
    private String date;

    public String getDate() {
        return date;
    }
}
