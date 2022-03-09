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

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.HistoryAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Order;
import com.example.vegeyuk.marketresto.responses.ResponseOrder;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListHistory extends Fragment {

    Context mContext;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<Order> pesanList;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ApiService mApiService;

    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
        mContext = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        mApiService = ServerConfig.getAPIService();

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        getPesan();
    }

    public void getPesan() {
        String id_konsumen = user.get(SessionManager.ID_USER);
        ArrayList<String> status = new ArrayList<String>();
        status.add("selesai");
        status.add("batal");
        mApiService.getOrder(id_konsumen, status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        pesanList = response.body().getData();
                        adapter = new HistoryAdapter(mContext, pesanList);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    } else {
                        message.setVisibility(View.VISIBLE);
                        icon_message.setImageResource(R.drawable.msg_history);
                        title_message.setText("Anda Belum Merubah Sejarah");
                        sub_title_message.setText("Pesan dan Ubah Sejarah");
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable t) {
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps.. Tidak Ada Koneksi");
                sub_title_message.setText("Priksa Kembali Koneksi Internet Anda");
                progressDialog.dismiss();
            }
        });

    }
}
