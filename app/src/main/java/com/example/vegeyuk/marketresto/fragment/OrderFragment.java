package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.OrderAdapter;
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


public class OrderFragment extends Fragment {

    Context mContext;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> pesanList;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ApiService mApiService;
    private static final String TAG = "SignUpActivity";

    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mContext = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        mApiService = ServerConfig.getAPIService();


        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);


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
        status.add("proses");
        status.add("pengantaran");

        mApiService.getOrder(id_konsumen, status, null, null).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        pesanList = response.body().getData();
                        adapter = new OrderAdapter(mContext, pesanList);

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        message.setVisibility(View.VISIBLE);
                        icon_message.setImageResource(R.drawable.msg_order);
                        title_message.setText("Ayo Pesan Makanan Anda Sekarang");
                        sub_title_message.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesanan Anda \n");
                        progressDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps.. Tidak Ada Koneksi");
                sub_title_message.setText("Priksa Kembali Koneksi Internet Anda");
                progressDialog.dismiss();
            }
        });


    }

}
