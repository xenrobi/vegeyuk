package com.example.vegeyuk.marketresto.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends AbsRunTimePermission implements LocationListener {

    private static final int REQUEST_PERMISSION = 10;

    private final Context mContext;

    //flag for GPS status
    boolean isGPSEnabled = false;
    //flag for Network Status
    boolean isNetworkEnabled = false;
    //flag can find location
    boolean canGetLocation = false;

    Location location;              //location
    double latitude, longitude;     //Lat,lang

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10; //Minutes
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;  //1000*60*1 = 1minute

    //Store LocationManager.GPS PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;

    //Declaring a Location Manager
    protected LocationManager locationManager;
    private int geocoderMaxResult = 1;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting Network satus
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled || !isNetworkEnabled) {

                requestAppPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE
                }, R.string.msg, REQUEST_PERMISSION);

            } else {
                this.canGetLocation = true;
            if (isNetworkEnabled){
                this.canGetLocation = true;
                provider_info = LocationManager.NETWORK_PROVIDER;
                Toast.makeText(mContext, "use GPS PRoV", Toast.LENGTH_SHORT).show();
            } else if (isGPSEnabled) {
                this.canGetLocation = true;
                provider_info = LocationManager.GPS_PROVIDER;
                Toast.makeText(mContext, "use NETWORK PRoV", Toast.LENGTH_SHORT).show();
            }

            if (!provider_info.isEmpty()) {


//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        requestAppPermissions(new String[]{
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.ACCESS_FINE_LOCATION,
//                                Manifest.permission.ACCESS_NETWORK_STATE
//                        }, R.string.msg, REQUEST_PERMISSION);
//
//                        return;
//                    }

                locationManager.requestLocationUpdates(provider_info, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                }

                if (locationManager != null){
                    location = locationManager.getLastKnownLocation(provider_info);
                    updateGPSCoordinates();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }


    public List<Address> getGeocoderAddress(Context context){
        if(location != null){
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,this.geocoderMaxResult);
                return addresses;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAddressLine (Context context){
        List<Address> addresses = getGeocoderAddress(context);

        if(addresses != null && addresses.size() > 0){
            Address address = addresses.get(0);
            String addresLine = address.getAddressLine(0);
            return addresLine;
        }else {
            return "Lokasi Tidak Ditemukan" ;
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Atur Layanan GPS Anda");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Layanan GPS Tidak Aktif. Apakah Anda ingin masuk ke menu pengaturan?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        final AlertDialog alert = alertDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            }
        });
        alert.show();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onPermissionGranted(int requestcode) {

    }
}
