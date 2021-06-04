package com.example.nhadat_app.Model;

public class UsSave {
    private int id;
    private String category;

    public UsSave(String category) {
        this.category = category;
    }

    public UsSave() {
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
