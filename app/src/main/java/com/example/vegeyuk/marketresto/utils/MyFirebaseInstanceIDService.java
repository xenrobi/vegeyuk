package com.example.vegeyuk.marketresto.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIDService";

   /* @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        //mendapatkan token registrasi
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: "+refreshedToken);
        storeToken(refreshedToken);
    }*/

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("NEW_TOKEN",s);
    }
    //untuk menyimpan token ke sharedpreferences
    private void storeToken(String token){

        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);

    }
}
