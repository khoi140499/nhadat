package com.example.nhadat_app.Model;

import android.net.Uri;

public class ImageSlide {
    private int url;
    private Uri url1;
    public ImageSlide(Integer url) {
        this.url = url;
    }

    public ImageSlide(Uri url1) {
        this.url1 = url1;
    }

    public Uri getUrl1() {
        return url1;
    }

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
    }
}
