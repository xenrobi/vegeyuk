package com.example.vegeyuk.marketresto.fragment;

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
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.MenuAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Kategori;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener {


    private RecyclerView mRecylerView;
    private MenuAdapter mAdapter;
    private List<Menu> menuList = new ArrayList<>();
    private List<Menu> menuListTemp = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    private MenuAdapter.OnItemClickListener listener;
    ApiService mApiService;
    //    TextView pos;
    int position, oprasional;
    SessionManager sessionManager;
    HashMap<String, String> user;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetail();
        mApiService = ServerConfig.getAPIService();
        menuList = (List<Menu>) getArguments().getSerializable("menu");
        kategoriList = (List<Kategori>) getArguments().getSerializable("kategori");
        oprasional = getArguments().getInt("oprasinal");
        position = getArguments().getInt("position");
        mRecylerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        listener = this;
        setByKategori();
        mAdapter = new MenuAdapter(getActivity(), menuListTemp, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.setItemAnimator(new DefaultItemAnimator());
        mRecylerView.setAdapter(mAdapter);
//        pos = (TextView) view.findViewById(R.id.position);

//        setValue(getArguments().getString("id_resto"));
    }


    public void setByKategori() {


        for (int i = 0; i < kategoriList.size(); i++) {
            if (position == i) {
                for (int j = 0; j < menuList.size(); j++) {
                    if (menuList.get(j).getIdKategori().toString().equals(kategoriList.get(i).getId().toString())) {
                        menuListTemp.add(menuList.get(j));
                    }
                }
            }
        }
    }

    @Override
    public void onItemCliked(final View v, final int position, boolean isLongClick) {
        final Menu menuItem = menuListTemp.get(position);
        String id_konsumen = user.get(SessionManager.ID_USER).toString();
        if (isLongClick) {
            if (menuItem.getMenuFavorit() > 0) {
                Toast.makeText(getActivity(), "Menu Sudah Jadi Favorit", Toast.LENGTH_SHORT).show();
            } else {
                //set favorit
                mApiService.setFavorit(id_konsumen, menuItem.getId().toString()).enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if (response.isSuccessful()) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            if (value.equals("1")) {
                                mAdapter.favoritAt(v, position);
                                menuItem.setMenuFavorit(1);
                                ImageView heart = (ImageView) v.findViewById(R.id.imgLove);
                                heart.setImageResource(R.drawable.f4);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.lostconnection, Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } else {
            Bundle argument = new Bundle();
            argument.putSerializable("selectedItem", menuItem);
            //oprasinal buka
            if (oprasional == 1) {
                DialogPlaceOrderFragment placeOrderFragment = new DialogPlaceOrderFragment();
                placeOrderFragment.setArguments(argument);
                placeOrderFragment.show(getFragmentManager(), DialogPlaceOrderFragment.ARG_ITEM_ID);
                //oprasional tutup
            } else {
                Toast.makeText(getActivity(), "TUTUP", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

