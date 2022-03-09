package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.FavoritAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Favorit;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseFavorit;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListFavorit extends Fragment implements FavoritAdapter.ClickListener {

    private RecyclerView recyclerView;
    private FavoritAdapter favoritAdapter;
    private List<Favorit> favoritList = new ArrayList<>();
    ApiService mApiService;
    Context mContext;
    FavoritAdapter.ClickListener listener;
    HashMap<String, String> user;
    SessionManager sessionManager;
    String id_konsumen;

    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_favorite, container, false);
        mApiService = ServerConfig.getAPIService();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        listener = this;
        id_konsumen = user.get(SessionManager.ID_USER);

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        setValue(id_konsumen);
    }

    private void setValue(String id_konsumen) {
        mApiService.getFavorit(id_konsumen).enqueue(new Callback<ResponseFavorit>() {
            @Override
            public void onResponse(Call<ResponseFavorit> call, Response<ResponseFavorit> response) {
                if (response.isSuccessful()) {
                    favoritList = response.body().getFavorit();
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        favoritAdapter = new FavoritAdapter(mContext, favoritList, listener);
                        recyclerView.setAdapter(favoritAdapter);
                        progressDialog.dismiss();
                    } else {
                        message.setVisibility(View.VISIBLE);
                        icon_message.setImageResource(R.drawable.msg_favorite);
                        title_message.setText("Anda Belum Memiliki Favorit");
                        sub_title_message.setText("Simpan menu favorit anda dari restoran favorit anda");
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFavorit> call, Throwable t) {
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps.. Tidak Ada Koneksi");
                sub_title_message.setText("Priksa Kembali Koneksi Internet Anda");
                progressDialog.dismiss();
            }
        });


    }


    @Override
    public void itemDeleted(View view, final int position) {
        Favorit favorit = (Favorit) favoritList.get(position);

        mApiService.deleteFavorit(favorit.getId()).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        favoritAdapter.removeAt(position);
                        //cartList.remove(position);
                        Toast.makeText(mContext, "Item Berhasil Dihapus", Toast.LENGTH_SHORT).show();

                        checFavorit();
                    } else {
                        Toast.makeText(mContext, "Item Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(mContext, R.string.lostconnection, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void itemClick(View view, int position) {
        Favorit favorit = favoritList.get(position);
        Menu menuItem = favorit.getFavoritMenu();
        Restoran restoran = favorit.getFavoritRestoran();
        Bundle argument = new Bundle();
        argument.putSerializable("selectedItem", menuItem);
        //oprasinal buka
        if (restoran.getRestoranOprasional() == 1) {

            if (menuItem.getMenuKetersediaan() == 0) {
                Toast.makeText(getActivity(), "Item Sedang Tidak Tersedia", Toast.LENGTH_SHORT).show();
            } else {
                DialogPlaceOrderFragment placeOrderFragment = new DialogPlaceOrderFragment();
                placeOrderFragment.setArguments(argument);
                placeOrderFragment.show(getFragmentManager(), DialogPlaceOrderFragment.ARG_ITEM_ID);
            }

            //oprasional tutup
        } else {
            Toast.makeText(getActivity(), "Restoran Sedang Tutup", Toast.LENGTH_SHORT).show();
        }
    }

    private void checFavorit() {

        //set error untuk melihat pesan kosong
//                          emtyFavorites.setVisibility(View.VISIBLE);
        if (favoritAdapter.getItemCount() == 0) {
            Toast.makeText(mContext, "Item Kosong", Toast.LENGTH_SHORT).show();
            message.setVisibility(View.VISIBLE);
            icon_message.setImageResource(R.drawable.msg_favorite);
            title_message.setText("Anda Belum Memiliki Favorit");
            sub_title_message.setText("Simpan menu favorit anda dari restoran favorit anda");
        }
//                          imgEmptyCart.setVisibility(View.VISIBLE);
//                           scCart.setVisibility(View.GONE);
    }


}
