package com.example.nhadat_app.Model;

import com.parse.ParseObject;

public class ImagePost {
    private String objectID;
    private String url;
    private ParseObject post_id;

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

    public ImagePost(ParseObject post_id, String url) {
        this.url = url;
        this.post_id = post_id;
    }

    public ParseObject getPost_id() {
        return post_id;
    }
}
