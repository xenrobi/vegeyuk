package com.example.vegeyuk.marketresto.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh() {

        //mendapatkan token registrasi
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: "+refreshedToken);
        storeToken(refreshedToken);

    }

    //untuk menyimpan token ke sharedpreferences
    private void storeToken(String token){

        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);

    }
}
