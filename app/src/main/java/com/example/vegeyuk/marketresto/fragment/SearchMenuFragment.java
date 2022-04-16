package com.example.vegeyuk.marketresto.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.MenuSearchAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMenuFragment extends Fragment {


    AlertDialog.Builder dialog;
    AlertDialog myAlert;
    private List<Menu> menuList = new ArrayList<>();
    private RecyclerView recyclerView;

    MenuSearchAdapter adapter;
    Context mContext;
    Button btnRecomend;

    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message;

    ApiService mApiService;

    EditText mNamaRestoran, mAlamatRestoran, mPhoneRestoran;

    public static SearchMenuFragment newInstance() {
        return new SearchMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_search, container, false);
        mContext = getActivity();
        mApiService = ServerConfig.getAPIService();
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);
        btnRecomend = (Button) view.findViewById(R.id.btnLocation);

        menuList = (List<Menu>) getArguments().getSerializable("menu");

        if (menuList.size() == 0) {
            message.setVisibility(View.VISIBLE);

            icon_message.setImageResource(R.drawable.msg_failure_search);
            title_message.setText("Opps.. Hidangan Tidak Tersedia");
            sub_title_message.setText("Kami akan terus mengembangkan \n jangkauan kami terhadap restoran anda \n");
            btnRecomend.setVisibility(View.VISIBLE);
            btnRecomend.setText("Rekomendasi Restoran");
        } else {


            recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
            adapter = new MenuSearchAdapter(getActivity(), menuList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        btnRecomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRekomendasi();
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
