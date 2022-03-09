package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.CartListActivity;
import com.example.vegeyuk.marketresto.activities.MapsActivity;
import com.example.vegeyuk.marketresto.adapter.RestorantAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseRestoran;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.GPSTracker;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestoFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestorantAdapter adapter;
    private List<Restoran> data = new ArrayList<>();
    ApiService mApiService;
    TextView textview, title_error, sub_title_error;
    ImageButton openmap;
    ImageView img_msg;
    Button btnLocation;
    Context mContext;
    GPSTracker gpsTracker;
    String addressLine, lat, lang;

    View viewError;

    SearchView searchView = null;
    SearchView.OnQueryTextListener queryTextListener;

    ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resto, container, false);
        mContext = getActivity();
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MavenPro-Regular.ttf");

        textview = (TextView) view.findViewById(R.id.tvLokasiAnda);
        textview.setTypeface(type);
        mApiService = ServerConfig.getAPIService();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        openmap = (ImageButton) view.findViewById(R.id.openmap);
        btnLocation = (Button) view.findViewById(R.id.btnLocation);

        viewError = view.findViewById(R.id.error);
        img_msg = (ImageView) view.findViewById(R.id.img_msg);
        title_error = (TextView) view.findViewById(R.id.title_msg);
        sub_title_error = (TextView) view.findViewById(R.id.sub_title_msg);


        // addData("0.47101406701336923", "101.40498843044043");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
            }
        });

        btnLocation.setVisibility(View.VISIBLE);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SessionManager sessionManager = new SessionManager(mContext);
        HashMap<String, String> locationSession;
        locationSession = sessionManager.getLocation();
        textview.setText(locationSession.get(SessionManager.ALAMAT));

        Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
        //progress dialog
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        addData(locationSession.get(SessionManager.LAT), locationSession.get(SessionManager.LANG));
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(mContext,"on resume",Toast.LENGTH_SHORT).show();
//        gpsTracker = new GPSTracker(mContext);
//        String lat = null,lang = null;
//        if(gpsTracker.canGetLocation()){
//
//            //Gone Error
//            mError.setVisibility(View.GONE);
//
//            Bundle arguments = getArguments();
//            if(arguments != null && arguments.containsKey("aksi")){
//                textview.setText(  arguments.getString("aksi"));
//                latlng = locationSession.get(SessionManager.LATLANG);
//            }else {
//
//                //ambil line address
//                addressLine = gpsTracker.getAddressLine(mContext);
//                //set text
//                textview.setText(addressLine);
//                //set location to session
//                latlng = String.valueOf(gpsTracker.getLatitude()) + "," + String.valueOf(gpsTracker.getLongitude());
//                lat = String.valueOf(gpsTracker.getLatitude());
//                lang =  String.valueOf(gpsTracker.getLongitude());
//                sessionManager.setLocation(addressLine, latlng);
//                addData(lat,lang);
//                Toast.makeText(mContext, "onResume , " + latlng, Toast.LENGTH_SHORT).show();
//            }
//
//        }else {
//            gpsTracker.showSettingsAlert();
//            mError.setVisibility(View.VISIBLE);
//        }
//
//    }

    private void addData(String lat, String lang) {

        mApiService.getRestoran(lat, lang).enqueue(new Callback<ResponseRestoran>() {
            @Override
            public void onResponse(Call<ResponseRestoran> call, Response<ResponseRestoran> response) {

                String value = response.body().getValue();
                if (response.isSuccessful()) {
                    if (value.equals("1")) {
                        data = response.body().getData();
                        adapter = new RestorantAdapter(getActivity(), data);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    } else {
                        viewError.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "restoran tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRestoran> call, Throwable t) {
                viewError.setVisibility(View.VISIBLE);
                img_msg.setImageResource(R.drawable.msg_no_connection);
                title_error.setText("Opps.. Tidak Ada Koneksi");
                sub_title_error.setText("Priksa Kembali Koneksi Internet Anda");
                btnLocation.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_location_cart, menu);
//        MenuItem search = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        search(searchView);


    }
//
//    private void search(SearchView searchView) {
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s);
//                return true;
//            }
//        });
//    }


    //    //Cart Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.cart:
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_location:
                //progress dialog
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                onLocation();
                break;
        }
        return true;
    }


    void onLocation() {
        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.canGetLocation()) {
            addressLine = gpsTracker.getAddressLine(mContext);
            if (addressLine.equals("Lokasi Tidak Ditemukan")) {
                progressDialog.dismiss();
                Toast.makeText(mContext, addressLine, Toast.LENGTH_SHORT).show();
            } else {
                SessionManager sessionManager = new SessionManager(mContext);
                lat = String.valueOf(gpsTracker.getLatitude());
                lang = String.valueOf(gpsTracker.getLongitude());
                Toast.makeText(mContext, addressLine + "\n" + lat + "," + lang, Toast.LENGTH_SHORT).show();
                sessionManager.setLocation(addressLine, lat, lang);
                HashMap<String, String> locationSession;
                locationSession = sessionManager.getLocation();
                textview.setText(locationSession.get(SessionManager.ALAMAT));
                addData(locationSession.get(SessionManager.LAT), locationSession.get(SessionManager.LANG));
            }
        } else {
            progressDialog.dismiss();
            gpsTracker.showSettingsAlert();
        }
    }
}
