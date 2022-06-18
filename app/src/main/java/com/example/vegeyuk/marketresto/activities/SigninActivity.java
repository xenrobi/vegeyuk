package com.example.vegeyuk.marketresto.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.User;
import com.example.vegeyuk.marketresto.responses.ResponseAuth;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.buttonGetVerificationCode)
    Button btnGVC;
    @BindView(R.id.textSignUp)
    TextView textSignUp;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    Context mContext;
    String value, message;
    ApiService mApiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mContext = this;
        ButterKnife.bind(this);
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(this);
        //Firebase Auth Instance

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MavenPro-Regular.ttf");
        editTextPhone.setTypeface(type);
        btnGVC.setTypeface(type);

    }


    @OnClick(R.id.buttonGetVerificationCode)
    void getCode() {
        //hidden keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

        //progress dialog
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);

        final String phone = clearPhone(editTextPhone.getText().toString());

        if (phone.equals("+62")) {
            progressDialog.dismiss();
            editTextPhone.setError("Nomor telepon diperlukan");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() < 13) {
            progressDialog.dismiss();
            editTextPhone.setError("Nomor telepon tidak valid");
            editTextPhone.requestFocus();
            return;
        }

        mApiService.signinRequest(phone).enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    value = response.body().getValue();
                    message = response.body().getMessage();
                    //phone terdaftar
                    if (value.equals("1")) {
                        //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        User user = response.body().getData();
                        Intent intent = new Intent(SigninActivity.this, VerifyActifity.class);
                        intent.putExtra("user", user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        //nomor phone tidak terdaftar
                    } else {
                        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE).setAction("Daftar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String hp = phone.substring(2, phone.length());
                                Intent intent = new Intent(mContext, SignUpActivity.class);
                                intent.putExtra("phone", hp);
                                startActivity(intent);
                            }
                        }).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, R.string.lostconnection, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.textSignUp)
    void toSignUp() {
        Intent intent = new Intent(mContext, SignUpActivity.class);

        startActivity(intent);

    }


    //untuk menggambil no hp
    public String clearPhone(String phoneNumber) {
        String hp = phoneNumber.replaceAll("-", "");
        String clearPhone = hp.substring(1, hp.length());
        return clearPhone;
    }


}
