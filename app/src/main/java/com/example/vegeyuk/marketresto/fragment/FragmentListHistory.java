package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.HistoryAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Order;
import com.example.vegeyuk.marketresto.responses.ResponseOrder;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Date;
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
    String dateStringS, dateStringE;
    EditText tanggal;


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
        tanggal =  (EditText) view.findViewById(R.id.tanggal);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        // now define the properties of the
        // materialDateBuilder
        materialDateBuilder.setTitleText("Pilih Tanggal");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override public void onPositiveButtonClick(Pair<Long,Long> selection) {
                Long startDate = selection.first;
                Long endDate = selection.second;
                dateStringS = DateFormat.format("yyyy-MM-dd", new Date(startDate)).toString();
                dateStringE = DateFormat.format("yyyy-MM-dd", new Date(endDate)).toString();
                tanggal.setText(materialDatePicker.getHeaderText());
                progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
                getPesan();
//                Toast.makeText(getContext(), dateStringS+" "+dateStringE, Toast.LENGTH_SHORT).show();
                //Do something...
            }
        });


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
        mApiService.getOrder(id_konsumen, status,dateStringS,dateStringE).enqueue(new Callback<ResponseOrder>() {
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
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
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
