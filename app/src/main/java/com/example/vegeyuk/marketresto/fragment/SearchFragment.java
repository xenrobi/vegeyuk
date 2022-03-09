package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.CartListActivity;
import com.example.vegeyuk.marketresto.adapter.PagerAdapterTabSearch;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseSearch;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    AlertDialog.Builder dialog;
    AlertDialog myAlert;
    Context mContext;
    SearchView searchView;
    TabLayout tabLayout;
    ApiService mApiService;
    SessionManager sessionManager;
    HashMap<String, String> locationSession;
    ProgressDialog progressDialog;
    private List<Restoran> restoranList = new ArrayList<>();
    private List<Menu> menuList = new ArrayList<>();
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;


    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message;
    Button btnRecomend;

    ImageButton cart;

    EditText mNamaRestoran, mAlamatRestoran, mPhoneRestoran;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        searchView = (SearchView) view.findViewById(R.id.serchview);
        sessionManager = new SessionManager(mContext);
        locationSession = sessionManager.getLocation();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);

        viewPager = view.findViewById(R.id.pager);
        cart = (ImageButton) view.findViewById(R.id.cart);

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);
        btnRecomend = (Button) view.findViewById(R.id.btnLocation);

        icon_message.setImageResource(R.drawable.msg_search_food);
        title_message.setText("Temukan Favorit di Sekitar Anda");
        btnRecomend.setVisibility(View.VISIBLE);
        btnRecomend.setText("Rekomendasi Restoran");


        mApiService = ServerConfig.getAPIService();
        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MavenPro-Regular.ttf");
        searchText.setTypeface(myCustomFont);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
                //progress dialog
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.cari), true, false);
                cari(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnRecomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRekomendasi();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void cari(String query) {
        progressDialog.dismiss();
        String lat = locationSession.get(SessionManager.LAT);
        String lang = locationSession.get(SessionManager.LANG);
        mApiService.cari(lat, lang, query).enqueue(new Callback<ResponseSearch>() {
            @Override
            public void onResponse(Call<ResponseSearch> call, Response<ResponseSearch> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        message.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        restoranList = response.body().getRestoran();
                        menuList = response.body().getMenu();

                        tabLayout.getTabAt(0).setText("Restoran (" + restoranList.size() + ")");
                        tabLayout.getTabAt(1).setText("Menu (" + menuList.size() + ")");


                        PagerAdapterTabSearch pagerAdapterTabSearch = new PagerAdapterTabSearch(getFragmentManager(), tabLayout.getTabCount(), menuList, restoranList);
                        viewPager.setAdapter(pagerAdapterTabSearch);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                    } else {
                        viewPager.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        message.setVisibility(View.VISIBLE);
                        btnRecomend.setVisibility(View.VISIBLE);
                        btnRecomend.setText("Ayo Rekomendasikan Favorit Anda");

                        icon_message.setImageResource(R.drawable.msg_failure_search);
                        title_message.setText("Opps.. Maaf Kami Belum Menemukannya");
                        sub_title_message.setText("Kami akan terus mengembangkan \n jangkauan kami terhadap restoran anda \n");
                        Toast.makeText(mContext, "Tidak ditmeukan", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseSearch> call, Throwable t) {
                progressDialog.dismiss();
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                btnRecomend.setVisibility(View.GONE);


                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps.. Gagal terhubung keserver");
                sub_title_message.setText("Priksa kembali koneksi internet Anda");
            }
        });

    }


    //form rekomendasi restoran
    private void DialogRekomendasi() {
        dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rekomendasi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Rekomendasi Restoran");

        mNamaRestoran = (EditText) dialogView.findViewById(R.id.etNamaRestoran);
        mAlamatRestoran = (EditText) dialogView.findViewById(R.id.etAlamatRestoran);
        mPhoneRestoran = (EditText) dialogView.findViewById(R.id.etPhoneRestoran);


        dialog.setPositiveButton("Rekomendasi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //insert new kurir

                String nama = mNamaRestoran.getText().toString();
                String alamat = mAlamatRestoran.getText().toString();
                String phone = mPhoneRestoran.getText().toString();

                setRekomendasi(nama, alamat, phone);


            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myAlert.dismiss();

            }
        });

        myAlert = dialog.create();
        myAlert.show();
    }

    private void setRekomendasi(String nama, String alamat, String phone) {

        mApiService.rekomendasi(nama, phone, alamat).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equals("1")) {
                        Toast.makeText(mContext, "Terimakasih atas rekomendasi Anda", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Gagal Rekomendasi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(mContext, R.string.lostconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
