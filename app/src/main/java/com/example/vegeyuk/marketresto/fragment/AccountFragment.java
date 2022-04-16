package com.example.vegeyuk.marketresto.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.EditKonsumen;
import com.example.vegeyuk.marketresto.activities.PenilaianActivity;
import com.example.vegeyuk.marketresto.activities.SigninActivity;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.User;
import com.example.vegeyuk.marketresto.responses.ResponseKonsumen;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    Context mContext;
    SessionManager sessionManager;
    TextView tvNamaUser, tvPhoneUser, tvEmailUser, tvBalance, btnBantuan, btnLayanan, btnPrivasi, btnPenialaian, signout;
    ImageButton edit;
    HashMap<String, String> user;
    ApiService mApiService;
    User user1 = new User();

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        mContext = getActivity();
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();

        signout = (TextView) view.findViewById(R.id.btn_sign_out);


        init(view);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertKonfirmasi();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditKonsumen.class);
                startActivity(intent);
            }
        });

        btnPenialaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PenilaianActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setValue() {
        tvNamaUser.setText(user.get(SessionManager.NAMA_LENGKAP));
        tvPhoneUser.setText("+" + user.get(SessionManager.NO_HP));
        tvEmailUser.setText(user.get(SessionManager.EMAIL));
        tvBalance.setText("Rp.-");

    }

    private void init(View view) {
        tvNamaUser = (TextView) view.findViewById(R.id.tvNamaUser);
        tvPhoneUser = (TextView) view.findViewById(R.id.tvPhoneUser);
        tvEmailUser = (TextView) view.findViewById(R.id.tvEmailUser);
        tvBalance = (TextView) view.findViewById(R.id.tvBalance);
        edit = (ImageButton) view.findViewById(R.id.edit);
        btnPenialaian = (TextView) view.findViewById(R.id.btnPenilaian);
    }


    //    @OnClick(R.id.btn_sign_out) void signOut (){
//        FirebaseAuth.getInstance().signOut();
//        sessionManager.logoutUser();
//        Intent intent = new Intent(mContext, SigninActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//    }

    public void getKonsumen() {
        mApiService.getKonsumen(user.get(SessionManager.ID_USER)).enqueue(new Callback<ResponseKonsumen>() {
            @Override
            public void onResponse(Call<ResponseKonsumen> call, Response<ResponseKonsumen> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equals("1")) {
                        user1 = response.body().getData();
                        tvBalance.setText(kursIndonesia(Double.parseDouble(user1.getKonsumenBalance())));
                        progressDialog.dismiss();
                    } else {
                        user1.setKonsumenBalance("Opps..Priksa Konseksi Internet Anda");
                        tvBalance.setText(user1.getKonsumenBalance());
                        tvBalance.setTextSize(13);
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKonsumen> call, Throwable t) {
                user1.setKonsumenBalance("Opps..Priksa Konseksi Internet Anda");
                tvBalance.setText(user1.getKonsumenBalance());
                tvBalance.setTextSize(13);
                progressDialog.dismiss();
            }
        });
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
        setValue();
        getKonsumen();
    }

    public void alertKonfirmasi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Apakah Anda Yakin Akan Keluar");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server

                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                logout();
                // intentSucess();

            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });
        alert.show();
    }

    void logout() {
        FirebaseAuth.getInstance().signOut();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        user.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(mContext, "User account deleted.",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
        sessionManager.logoutUser();
        progressDialog.dismiss();
        Intent intent = new Intent(mContext, SigninActivity.class);
        startActivity(intent);
        getActivity().finish();

    }


}
