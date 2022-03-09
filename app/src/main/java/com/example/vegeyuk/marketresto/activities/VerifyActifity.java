package com.example.vegeyuk.marketresto.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.User;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.example.vegeyuk.marketresto.utils.SharedPrefManager;
import com.github.irvingryan.VerifyCodeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActifity extends AppCompatActivity {

    @BindView(R.id.editTextCode)
    VerifyCodeView editTextCode;
    @BindView(R.id.buttonSignIn)
    Button btnSigin;
    @BindView(R.id.textSendCode)
    TextView txtResend;

    PhoneAuthProvider.ForceResendingToken mResendToken;

    ApiService mApiService;


    Context mContext;
    FirebaseAuth mAuth;
    String codeSent;
    User user;
    ProgressDialog progressDialog;
    SessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mContext = this;
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user");
        txtResend.setText("Masukan kode verifikasi yang dikirim melalui \n SMS pada nomor ponsel +" + user.getKonsumenPhone());
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MavenPro-Regular.ttf");
        btnSigin.setTypeface(type);

        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        mAuth = FirebaseAuth.getInstance();


        Toast.makeText(mContext, user.getKonsumenPhone(), Toast.LENGTH_SHORT).show();
//      Memanggil method untuk mengirim code
        // sendVerificationCode(user.getKonsumenPhone());
    }


    //    On click sign in button
    @OnClick(R.id.buttonSignIn)
    void signin() {
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
//        untuk melakukan verifikasi dari code OTP yang di inputkan
        //verifySignInCode();
//        jika menguji login tanpa menggunakan code OTP
        SessionUser();


    }


    private void verifySignInCode() {
        String code = editTextCode.getText();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //to sucses login
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "login successfuli", Toast.LENGTH_LONG).show();
                            SessionUser();

                            // ...
                        } else {

                            // The verification code entered was invalid
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Incorrect Verificarion Code", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }


    private void sendVerificationCode(String phone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            Toast.makeText(mContext, "verification completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(mContext, "verification fialed", Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                Toast.makeText(mContext, "invalid mob no", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                Toast.makeText(mContext, "quota over", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(mContext, "Code sent", Toast.LENGTH_SHORT).show();
            codeSent = s;
            mResendToken = forceResendingToken;
        }
    };

    @OnClick(R.id.resendCode)
    void onResendCode() {
        ResendCode(user.getKonsumenPhone());
    }

    public void ResendCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                1,               // Timeout duration
                TimeUnit.MINUTES,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);             // Force Resending Token from callbacks
    }


    //sessionLogin
    private void SessionUser() {
        sessionManager.createLoginSession(user);
        updateToken();

        //Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();


    }


    private void updateToken() {
        String Token = SharedPrefManager.getInstance(this).getDeviceToken();
        mApiService.updateToken(user.getId().toString(), Token).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        Toast.makeText(mContext, "Update Token Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        VerifyActifity.this.finish();
                    } else {
                        Toast.makeText(mContext, "Update Token Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(mContext, "Update Token Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(mContext, "on Pause",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(mContext, "on Resume",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(mContext, "on stop",Toast.LENGTH_SHORT).show();
//    }
}
