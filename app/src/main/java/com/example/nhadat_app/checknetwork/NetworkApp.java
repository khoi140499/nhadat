package com.example.nhadat_app.checknetwork;

import android.app.Application;

public class NetworkApp extends Application {
    static NetworkApp network;

    @Override
    public void onCreate() {
        super.onCreate();
        network=this;
    }
    public static synchronized NetworkApp getInstance(){
        return network;
    }
}
