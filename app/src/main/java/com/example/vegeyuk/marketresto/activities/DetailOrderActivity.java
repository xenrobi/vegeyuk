package com.example.vegeyuk.marketresto.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.DetailOrderAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Order;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.NonScrollListView;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {


    private List<Menu> detailOrders = new ArrayList<>();
    private DetailOrderAdapter orderAdapter;
    private Order pesan;
    ApiService mApiService;
    Context mContext;
    ProgressDialog progressOrder;


    @BindView(R.id.listview)
    NonScrollListView list;
    @BindView(R.id.tvNamaKonsumen)
    TextView mNamaKonsumen;
    @BindView(R.id.tvPhoneKonsumen)
    TextView mPhoneKonsumen;
    @BindView(R.id.tvAlamatAntar)
    TextView mAlamat;
    @BindView(R.id.tvSubTotal)
    TextView mSubTotal;
    @BindView(R.id.tvBiayaAntar)
    TextView mBiayaAntar;
    @BindView(R.id.tvTotal)
    TextView mTotal;
    @BindView(R.id.tvMetodeBayar)
    TextView mMetodeBayar;
    @BindView(R.id.btnInvoice)
    TextView btnInvoice;
    @BindView(R.id.layoutCatatan)
    LinearLayout layoutCatatn;
    @BindView(R.id.catatan)
    TextView mCatatan;
    @BindView(R.id.layoutStatus)
    LinearLayout layoutStatus;

    @BindView(R.id.tvStatusSelesai)
    TextView statusSelesai;
    @BindView(R.id.tvStatusBatal)
    TextView statusBatal;
    @BindView(R.id.tvStatusPengantaran)
    TextView statusPengantaran;
    @BindView(R.id.tvStatusProses)
    TextView statusProses;

    @BindView(R.id.viewpb1)
    View viewpb1;
    @BindView(R.id.lyt_pb1)
    LinearLayout lyt_pb1;
    @BindView(R.id.pajak_pb1)
    TextView pajak_pb1;
    @BindView(R.id.tvPajakPB1)
    TextView tvPajakPB1;

    SessionManager sessionManager;

    ProgressDialog progressDialog, progressSendEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
        mContext = this;
        sessionManager = new SessionManager(mContext);
        mApiService = ServerConfig.getAPIService();
        setListViewHeightBasedOnChildren(list);
        getIncomingIntent();
        orderAdapter = new DetailOrderAdapter(DetailOrderActivity.this, detailOrders);
        list.setAdapter(orderAdapter);

        setData();


        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressSendEmail = ProgressDialog.show(mContext, null, "Mengirim ke email...", true, false);
                sendEmail(pesan.getId());
            }
        });


    }


    private void setData() {
        String id_order = pesan.getId().toString();
        mNamaKonsumen.setText(pesan.getOrderKonsumen());
        mPhoneKonsumen.setText("+" + pesan.getOrderKonsumenPhone());
        mAlamat.setText(pesan.getOrderAlamat());
        Double subtotal = 0.0;
        for (int i = 0; i < detailOrders.size(); i++) {
            if (detailOrders.get(i).getMenuDiscount() == 0 || detailOrders.get(i).getMenuDiscount().toString().isEmpty()) {
                subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();
            } else {
                Double harga_discount = HitungDiscount(Double.parseDouble(detailOrders.get(i).getPivot().getHarga()), detailOrders.get(i).getPivot().getDiscount());
                subtotal += harga_discount * detailOrders.get(i).getPivot().getQty();
            }
        }
        mSubTotal.setText(kursIndonesia(subtotal));
        mBiayaAntar.setText(kursIndonesia(Double.parseDouble(pesan.getOrderBiayaAnatar())));
        double total = subtotal + Double.parseDouble(pesan.getOrderBiayaAnatar());

        //cek pajak
        if (pesan.getOrder_pajak_pb_satu() == 0) {
            viewpb1.setVisibility(View.GONE);
            lyt_pb1.setVisibility(View.GONE);
        } else {
            viewpb1.setVisibility(View.VISIBLE);
            lyt_pb1.setVisibility(View.VISIBLE);
            pajak_pb1.setText("PB1 (" + pesan.getOrder_pajak_pb_satu() + "%)");
            double pb1 = (pesan.getOrder_pajak_pb_satu() / 100.0) * total;
            tvPajakPB1.setText(kursIndonesia(pb1));
            total = total + pb1;
        }

        mTotal.setText(kursIndonesia(total));
        mMetodeBayar.setText(pesan.getOrderMetodeBayar());
        //catatan
        if (pesan.getOrderCatatan() != null) {
            layoutCatatn.setVisibility(View.VISIBLE);
            mCatatan.setText(pesan.getOrderCatatan().toString());

        }

        if (pesan.getOrderStatus().equalsIgnoreCase("selesai")) {
            statusSelesai.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.VISIBLE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("batal")) {
            statusBatal.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("pengantaran")) {
            statusPengantaran.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("proses")) {
            statusProses.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        }

    }

    //get inten comming
    private void getIncomingIntent() {

        if (getIntent().hasExtra("pesan")) {


            pesan = (Order) getIntent().getSerializableExtra("pesan");
            detailOrders = pesan.getDetailOrder();

        }
    }


    //listview not scrolll
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    //konfersi ke mata uang rupiah
    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;


    }

    public Double HitungDiscount(Double Harga, Integer Discount) {
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }

    void sendEmail(Integer id_order) {
        mApiService.sendEmail(id_order).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressSendEmail.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getValue().equals("1")) {
                        Toast.makeText(mContext, "Berhasil,Cek Email Anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressSendEmail.dismiss();
            }
        });
    }


}
