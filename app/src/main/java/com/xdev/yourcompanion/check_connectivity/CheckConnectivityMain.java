package com.xdev.yourcompanion.check_connectivity;

import android.app.Application;


public class CheckConnectivityMain extends Application {
    private static CheckConnectivityMain mInstance;

    public static synchronized CheckConnectivityMain getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}
