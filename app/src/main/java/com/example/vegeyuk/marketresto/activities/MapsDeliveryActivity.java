package com.example.vegeyuk.marketresto.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.Delivery;
import com.example.vegeyuk.marketresto.models.Order;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsDeliveryActivity extends FragmentActivity implements OnMapReadyCallback {

    //variable yang mereference ke firebase realtime database
    private DatabaseReference database;
    private Order pesan;
    private GoogleMap mMap;
    LatLng manDelivery;
    Marker deliverMarker;
    LatLng tujuan;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_delivery);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        getIncomingIntent();

        tujuan = new LatLng(Double.parseDouble(pesan.getOrderLat().toString()), Double.parseDouble(pesan.getOrderLong().toString()));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        //mengambil referensi ke firebase database
        database = FirebaseDatabase.getInstance().getReference();

        database.child("delivery").child(pesan.getId().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Delivery delivery = dataSnapshot.getValue(Delivery.class);
                manDelivery = new LatLng(Double.parseDouble(delivery.getLat().toString()), Double.parseDouble(delivery.getLng().toString()));


                mapFragment.getMapAsync(MapsDeliveryActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        // deliver = mMap.addMarker(new MarkerOptions().position(manDelivery).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manDelivery,15));

        //LatLng pickUpLatLng = new LatLng(0.488606, 101.397568);
        //LatLng locationLatLng = new LatLng(0.487404, 101.396592);


        // Tambah Marker
        mMap.addMarker(new MarkerOptions().position(tujuan).title("Lokasi Tujuan"));

        if (deliverMarker != null) {
            deliverMarker.remove();
        }

        deliverMarker = mMap.addMarker(new MarkerOptions().position(manDelivery).title("Lokasi Pengantar"));
//        mMap.addPolyline(new PolylineOptions()
//                .add(pickUpLatLng, manDelivery)
//                .width(5)
//                .color(Color.RED));
        /** START
         * Logic untuk membuat layar berada ditengah2 dua koordinat
         */

        //ukuran marker
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.msg_order);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 65, 65, false);

        deliverMarker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));


        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
        latLongBuilder.include(tujuan);
        latLongBuilder.include(manDelivery);

        // Bounds Coordinata
        LatLngBounds bounds = latLongBuilder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int paddingMap = (int) (width * 0.2); //jarak dari
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
        mMap.animateCamera(cu);
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("pesan")) {


            pesan = (Order) getIntent().getSerializableExtra("pesan");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
