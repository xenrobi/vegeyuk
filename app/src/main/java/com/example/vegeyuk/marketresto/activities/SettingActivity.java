package com.example.vegeyuk.marketresto.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {


    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        ButterKnife.bind(this);
        mContext = this;
        sessionManager = new SessionManager(mContext);
    }

    @OnClick(R.id.btn_sign_out)
    void signOut() {
        FirebaseAuth.getInstance().signOut();
        sessionManager.logoutUser();
        Intent intent = new Intent(mContext, SigninActivity.class);
        startActivity(intent);
        finish();
    }
}
