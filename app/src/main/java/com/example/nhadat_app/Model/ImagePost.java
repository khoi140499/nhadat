package com.example.nhadat_app.Model;

public class ImagePost {
    private String objectID;
    private String url;

    public ImagePost(String objectID, String url) {
        this.objectID = objectID;
        this.url = url;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getUrl() {
        return url;
    }
}
