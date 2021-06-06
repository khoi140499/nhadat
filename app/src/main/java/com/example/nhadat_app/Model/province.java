package com.example.nhadat_app.Model;

public class province {
    private String provinceID;
    private String province;

    public province(String provinceID, String province) {
        this.provinceID = provinceID;
        this.province = province;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
