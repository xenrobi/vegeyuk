package com.example.vegeyuk.marketresto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vegeyuk.marketresto.R;

public class SuccessOrder extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);
        TextView idOrder = (TextView) findViewById(R.id.idorder);
        TextView btnSelesai = (TextView) findViewById(R.id.btnSelesai);
        String id = getIntent().getStringExtra("id");
        idOrder.setText("Order number: #" + id);
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selesai();
            }
        });
    }

    @Override
    public void onBackPressed() {
        selesai();
    }

    public void selesai() {
        Intent intent = new Intent(SuccessOrder.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
