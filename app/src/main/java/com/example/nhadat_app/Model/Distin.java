package com.example.nhadat_app.Model;

public class Distin {
    private String provinceID;
    private String distinID;
    private String distin;

    public Distin(String provinceID, String distinID, String distin) {
        this.provinceID = provinceID;
        this.distinID = distinID;
        this.distin = distin;
    }

    public Distin(String distin) {
        this.distin = distin;
    }

    public Distin(String distinID, String distin) {
        this.distinID = distinID;
        this.distin = distin;
    }

    public Distin() {
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistinID() {
        return distinID;
    }

    public void setDistinID(String distinID) {
        this.distinID = distinID;
    }

    public String getDistin() {
        return distin;
    }

    public void setDistin(String distin) {
        this.distin = distin;
    }
}
