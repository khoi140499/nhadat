package com.example.nhadat_app.Model;

public class Ward {
    private String distinID;
    private String ward;

    public Ward(String distinID, String ward) {
        this.distinID = distinID;
        this.ward = ward;
    }

    public Ward(String ward) {
        this.ward = ward;
    }

    public String getDistinID() {
        return distinID;
    }

    public void setDistinID(String distinID) {
        this.distinID = distinID;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
