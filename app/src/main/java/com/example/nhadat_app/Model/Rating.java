package com.example.nhadat_app.Model;

public class Rating {
    private String namedg;
    private String namepost;
    private float rate;
    private String cmt;
    private String date;

    public Rating(String namedg, String namepost, float rate, String cmt, String date) {
        this.namedg = namedg;
        this.namepost = namepost;
        this.rate = rate;
        this.cmt = cmt;
        this.date = date;
    }

    public String getNamedg() {
        return namedg;
    }

    public String getNamepost() {
        return namepost;
    }

    public float getRate() {
        return rate;
    }

    public String getCmt() {
        return cmt;
    }

    public String getDate() {
        return date;
    }
}
