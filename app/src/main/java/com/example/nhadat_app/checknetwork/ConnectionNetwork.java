package com.example.nhadat_app.checknetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionNetwork extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public static boolean isConnected(){
        ConnectivityManager cm= (ConnectivityManager) NetworkApp.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        return ni!=null && ni.isConnectedOrConnecting();
    }
}
