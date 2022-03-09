package com.example.vegeyuk.marketresto.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenilaianActivity extends AppCompatActivity {

    @BindView(R.id.btn_submit)
    TextView btn_submit;

    @BindView(R.id.soal1)
    RadioGroup soal1;
    @BindView(R.id.soal2)
    RadioGroup soal2;
    @BindView(R.id.soal3)
    RadioGroup soal3;
    @BindView(R.id.soal4)
    RadioGroup soal4;
    @BindView(R.id.soal5)
    RadioGroup soal5;
    @BindView(R.id.soal6)
    RadioGroup soal6;

    ApiService mApiServie;
    SessionManager sessionManager;
    HashMap<String, String> user;


    RadioButton jawaban1, jawaban2, jawaban3, jawaban4, jawaban5, jawaban6;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penilaian);
        ButterKnife.bind(this);
        mApiServie = ServerConfig.getAPIService();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jawaban1 = (RadioButton) findViewById(R.id.soal1A);
                jawaban2 = (RadioButton) findViewById(R.id.soal2A);
                jawaban3 = (RadioButton) findViewById(R.id.soal3A);
                jawaban4 = (RadioButton) findViewById(R.id.soal4A);
                jawaban5 = (RadioButton) findViewById(R.id.soal5A);
                jawaban6 = (RadioButton) findViewById(R.id.soal6A);

                if (soal1.getCheckedRadioButtonId() == -1 || soal2.getCheckedRadioButtonId() == -1 ||
                        soal3.getCheckedRadioButtonId() == -1 ||
                        soal4.getCheckedRadioButtonId() == -1 ||
                        soal5.getCheckedRadioButtonId() == -1 ||
                        soal6.getCheckedRadioButtonId() == -1) {

                    if (soal1.getCheckedRadioButtonId() == -1) {

                        jawaban1.setError("Pilih Satu Jawaban");
                        jawaban1.setFocusable(true);
                    } else {
                        jawaban1.setError(null);
                    }

                    if (soal2.getCheckedRadioButtonId() == -1) {

                        jawaban2.setError("Pilih Satu Jawaban");
                        jawaban2.setFocusable(true);
                    } else {
                        jawaban2.setError(null);
                    }

                    if (soal3.getCheckedRadioButtonId() == -1) {

                        jawaban3.setError("Pilih Satu Jawaban");
                        jawaban3.setFocusable(true);
                    } else {
                        jawaban3.setError(null);
                    }


                    if (soal4.getCheckedRadioButtonId() == -1) {

                        jawaban4.setError("Pilih Satu Jawaban");
                        jawaban4.setFocusable(true);
                    } else {
                        jawaban4.setError(null);
                    }


                    if (soal5.getCheckedRadioButtonId() == -1) {

                        jawaban5.setError("Pilih Satu Jawaban");
                        jawaban5.setFocusable(true);
                    } else {
                        jawaban5.setError(null);
                    }
                    if (soal6.getCheckedRadioButtonId() == -1) {

                        jawaban6.setError("Pilih Satu Jawaban");
                        jawaban6.setFocusable(true);
                    } else {
                        jawaban6.setError(null);
                    }

                } else {
                    jawaban1.setError(null);
                    jawaban2.setError(null);
                    jawaban3.setError(null);
                    jawaban4.setError(null);
                    jawaban5.setError(null);
                    jawaban6.setError(null);

                    int int1 = soal1.getCheckedRadioButtonId();
                    int int2 = soal2.getCheckedRadioButtonId();
                    int int3 = soal3.getCheckedRadioButtonId();
                    int int4 = soal4.getCheckedRadioButtonId();
                    int int5 = soal5.getCheckedRadioButtonId();
                    int int6 = soal6.getCheckedRadioButtonId();

                    jawaban1 = (RadioButton) findViewById(int1);
                    jawaban2 = (RadioButton) findViewById(int2);
                    jawaban3 = (RadioButton) findViewById(int3);
                    jawaban4 = (RadioButton) findViewById(int4);
                    jawaban5 = (RadioButton) findViewById(int5);
                    jawaban6 = (RadioButton) findViewById(int6);


                    String str1 = jawaban1.getText().toString();
                    String str2 = jawaban2.getText().toString();
                    String str3 = jawaban3.getText().toString();
                    String str4 = jawaban4.getText().toString();
                    String str5 = jawaban5.getText().toString();
                    String str6 = jawaban6.getText().toString();

                    //Toast.makeText(PenilaianActivity.this,str1+","+str2+","+str3+","+str4+","+str5+","+str6,Toast.LENGTH_LONG).show();

                    insert(str1, str2, str3, str4, str5, str6);
                }


            }
        });
    }

    private void insert(String str1, String str2, String str3, String str4, String str5, String str6) {

        int nilai_1 = 0, nilai_2 = 0, nilai_3 = 0, nilai_4 = 0, nilai_5 = 0, nilai_6 = 0;

        if (str1.equalsIgnoreCase("Sangat Setuju")) {
            nilai_1 = 5;
        } else if (str1.equalsIgnoreCase("Setuju")) {
            nilai_1 = 4;
        } else if (str1.equalsIgnoreCase("Netral")) {
            nilai_1 = 3;
        } else if (str1.equalsIgnoreCase("Tidak Setuju")) {
            nilai_1 = 2;
        } else if (str1.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_1 = 1;
        }

        if (str2.equalsIgnoreCase("Sangat Setuju")) {
            nilai_2 = 5;
        } else if (str2.equalsIgnoreCase("Setuju")) {
            nilai_2 = 4;
        } else if (str2.equalsIgnoreCase("Netral")) {
            nilai_2 = 3;
        } else if (str2.equalsIgnoreCase("Tidak Setuju")) {
            nilai_2 = 2;
        } else if (str2.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_2 = 1;
        }

        if (str3.equalsIgnoreCase("Sangat Setuju")) {
            nilai_3 = 5;
        } else if (str3.equalsIgnoreCase("Setuju")) {
            nilai_3 = 4;
        } else if (str3.equalsIgnoreCase("Netral")) {
            nilai_3 = 3;
        } else if (str3.equalsIgnoreCase("Tidak Setuju")) {
            nilai_3 = 2;
        } else if (str3.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_3 = 1;
        }

        if (str4.equalsIgnoreCase("Sangat Setuju")) {
            nilai_4 = 5;
        } else if (str4.equalsIgnoreCase("Setuju")) {
            nilai_4 = 4;
        } else if (str4.equalsIgnoreCase("Netral")) {
            nilai_4 = 3;
        } else if (str4.equalsIgnoreCase("Tidak Setuju")) {
            nilai_4 = 2;
        } else if (str4.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_4 = 1;
        }

        if (str5.equalsIgnoreCase("Sangat Setuju")) {
            nilai_5 = 5;
        } else if (str5.equalsIgnoreCase("Setuju")) {
            nilai_5 = 4;
        } else if (str5.equalsIgnoreCase("Netral")) {
            nilai_5 = 3;
        } else if (str5.equalsIgnoreCase("Tidak Setuju")) {
            nilai_5 = 2;
        } else if (str5.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_5 = 1;
        }


        if (str6.equalsIgnoreCase("Sangat Setuju")) {
            nilai_6 = 5;
        } else if (str6.equalsIgnoreCase("Setuju")) {
            nilai_6 = 4;
        } else if (str6.equalsIgnoreCase("Netral")) {
            nilai_6 = 3;
        } else if (str6.equalsIgnoreCase("Tidak Setuju")) {
            nilai_6 = 2;
        } else if (str6.equalsIgnoreCase("Sangat Tidak Setuju")) {
            nilai_6 = 1;
        }


        String id_kons = user.get(SessionManager.ID_USER).toString();

        mApiServie.uat(id_kons, nilai_1, nilai_2, nilai_3, nilai_4, nilai_5, nilai_6).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equalsIgnoreCase("1")) {
                        Toast.makeText(PenilaianActivity.this, "Teriakasih Atas Penilaian Anda", Toast.LENGTH_SHORT).show();
                        soal1.clearCheck();
                        soal2.clearCheck();
                        soal3.clearCheck();
                        soal4.clearCheck();
                        soal5.clearCheck();
                        soal6.clearCheck();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(PenilaianActivity.this, R.string.lostconnection, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
