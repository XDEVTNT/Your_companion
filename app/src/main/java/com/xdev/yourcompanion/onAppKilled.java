package com.xdev.yourcompanion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class onAppKilled extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String userId = sharedpreferences.getString("did", "");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
        DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("driversWorking");
        GeoFire geoFireWorking = new GeoFire(refWorking);
        GeoFire geoFire = new GeoFire(ref);
        geoFireWorking.removeLocation(userId);
        geoFire.removeLocation(userId);
    }
}
