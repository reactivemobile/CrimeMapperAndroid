package com.reactivemobile.ukpoliceapp.objects;

import com.google.gson.annotations.Expose;

public class CrimeCategory {

    @Expose
    public String url;

    @Expose
    public String name;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
