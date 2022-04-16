package com.example.vegeyuk.marketresto.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.PagerAdapterTabKategoriMenuDynamic;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Kategori;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseMenu;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private Restoran resto;
    private ApiService mApiService;
    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    ProgressDialog progressDialog;
    String oprasional;
    SessionManager sessionManager;
    HashMap<String, String> user;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;


    Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mContext = this;
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();

        viewPager = findViewById(R.id.viewpager);


        mTabLayout = findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        ImageView imgResto = (ImageView) findViewById(R.id.imageRestoran);
        TextView mAlamat = (TextView) findViewById(R.id.tvAlamat);
        TextView mOperasional = (TextView) findViewById(R.id.tvOprasional);
        setSupportActionBar(toolbar);
        initViews();

        getIncomingIntent();
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);

        //set alamat restoran
        mAlamat.setText(resto.getRestoranAlamat());
        //set operasional restoran
        if (resto.getRestoranOprasional() == 1) {
            mOperasional.setText("BUKA");
            mOperasional.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            mOperasional.setText("TUTUP");
            mOperasional.setBackgroundResource(R.drawable.rounded_corner_red);
        }

        //set title toolbar
        collapsingToolbar.setTitle(resto.getRestoranNama());


        //set image background
        Picasso.get()
                .load(getResources().getString(R.string.path_restoran) + resto.getRestoranFoto())
                .into(imgResto);


        //get operasional restoran
        oprasional = resto.getRestoranOprasional().toString();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(PagerAdapterTabFavoritview, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(mContext, CartListActivity.class);
//                intent.putExtra("Resto", resto);
                startActivity(intent);
            }
        });
    }


    private void initViews() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        setDynamicFragmentToTabLayout();

    }

    private void setDynamicFragmentToTabLayout() {
        int jmlKate = kategoriList.size();


        for (int i = 0; i < jmlKate; i++) {

            mTabLayout.addTab(mTabLayout.newTab().setText(kategoriList.get(i).getKategoriNama() + " (" + kategoriList.get(i).getTotalMenu() + ")"));
        }


        PagerAdapterTabKategoriMenuDynamic mDynamicFragmentAdapter = new PagerAdapterTabKategoriMenuDynamic(getSupportFragmentManager(), mTabLayout.getTabCount(), menuList, kategoriList, oprasional);
        viewPager.setAdapter(mDynamicFragmentAdapter);
        viewPager.setCurrentItem(0);
    }


    private void getIncomingIntent() {

        if (getIntent().hasExtra("Resto")) {

            resto = (Restoran) getIntent().getSerializableExtra("Resto");
            String id_restoran = resto.getId().toString();
            String id_konsuemn = user.get(SessionManager.ID_USER);
            setValue(id_restoran, id_konsuemn);

//            Bundle bundle = new Bundle();
//            String id_resto = resto.getIdRestoran().toString();
//            bundle.putString("id_resto",id_resto);
//            RestoMenuFragment restoMenuFragment = new RestoMenuFragment();
//            restoMenuFragment.setArguments(bundle);
//            loadFragment(restoMenuFragment);
        }


    }


    private void setValue(String id_restorant, String id_konsumen) {

        mApiService.getRestoranMenu(id_restorant, id_konsumen).enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    if (value.equals("1")) {
                        menuList = response.body().getRestoranMenu();
                        kategoriList = response.body().getRestoranKategori();
                        initViews();
//                    mAdapter = new MenuAdapter(getActivity(),data,listener);
//                    mRecylerView.setAdapter(mAdapter);
//                    Toast.makeText(getContext(),"ok",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                progressDialog.dismiss();

            }
        });


    }

}



