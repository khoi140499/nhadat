package com.example.nhadat_app.Model;

public class Follow {
    private String objectID;
    private String user_id;
    private String user_following;

    public Follow(String objectID, String user_id, String user_following) {
        this.objectID = objectID;
        this.user_id = user_id;
        this.user_following = user_following;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_following() {
        return user_following;
    }
}
