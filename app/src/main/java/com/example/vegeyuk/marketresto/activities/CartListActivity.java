package com.example.vegeyuk.marketresto.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.adapter.CartAdapter;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.CartList;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseOneRestoran;
import com.example.vegeyuk.marketresto.responses.ResponseValue;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.example.vegeyuk.marketresto.utils.DatabaseHelper;
import com.example.vegeyuk.marketresto.utils.NonScrollListView;
import com.example.vegeyuk.marketresto.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity implements CartAdapter.ClickListener {


    private static final String TAG = "Cart List";
    DatabaseHelper myDb;
    Context mContext;
    SessionManager sessionManager;
    ApiService mApiService;
    ProgressDialog progressDialog, progressOrder, progressResto;

    private ArrayList<CartList> cartList;
    private ArrayList<CartList> cartListTemp;
    private List<Menu> menuList = new ArrayList<>();
    CartAdapter.ClickListener listener;
    private CartAdapter adapter;
    Restoran resto;
    double saldo;

    @BindView(R.id.listview)
    NonScrollListView list;

    @BindView(R.id.scCart)
    ScrollView scCart;
    @BindView(R.id.tvSubTotal)
    TextView mSubTotal;
    @BindView(R.id.tvBiayaAntar)
    TextView mBiayaAntar;
    @BindView(R.id.tvTotal)
    TextView mTotal;
    @BindView(R.id.tvNamaKonsumen)
    TextView mNamaKonsumen;
    @BindView(R.id.tvPhoneKonsumen)
    TextView mPhoneKonsumen;
    @BindView(R.id.tvAlamatAntar)
    TextView mAlamatAntar;
    @BindView(R.id.etCatatanAlamat)
    EditText mCatatanAlamat;
    @BindView(R.id.btnOrder)
    TextView btnOrder;
    @BindView(R.id.rgMetodeBayar)
    RadioGroup mMetodeBayar;
    RadioButton metodebayarButton, restopayButton;
    @BindView(R.id.btnCatatan)
    TextView mBtnCatatan;
    @BindView(R.id.tvSaldo)
    TextView mSaldo;
    @BindView(R.id.error)
    View message;
    @BindView(R.id.img_msg)
    ImageView img_msg;
    @BindView(R.id.title_msg)
    TextView title_msg;
    @BindView(R.id.sub_title_msg)
    TextView sub_title_msg;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnResto)
    ImageView btnResto;
    @BindView(R.id.viewpb1)
    View viewpb1;
    @BindView(R.id.lyt_pb1)
    LinearLayout lyt_pb1;
    @BindView(R.id.pajak_pb1)
    TextView pajak_pb1;
    @BindView(R.id.tvPajakPB1)
    TextView tvPajakPB1;


    double SubTotal, Total;
    HashMap<String, String> user, location;
    String konsumen_id, konsumen_nama, konsumen_phone, id_resto, tempMsgTarifAntar;
    Float hargaFloat;
    int jmlMenuTidakTersedia = 0, jmlCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        mContext = this;
        mApiService = ServerConfig.getAPIService();
        myDb = new DatabaseHelper(mContext);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        location = sessionManager.getLocation();
        progressDialog = new ProgressDialog(mContext);


        setListViewHeightBasedOnChildren(list);
        cartList = new ArrayList<CartList>();
        cartListTemp = new ArrayList<CartList>();
        listener = this;
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        cekCartList();


    }

    //get data cart from sqlite
    private void cekCartList() {
        //Mengambil data dari Sqlite
        Cursor res = myDb.getAllCart();
        jmlCart = res.getCount();
        //kondisi tidak ada cart
        if (jmlCart == 0) {
            progressDialog.dismiss();
            emptyCart();
            Toast.makeText(mContext, "anda tidak memiliki cart", Toast.LENGTH_SHORT).show();
            //kondisi ada cart
        } else {
            // res.moveToFirst();//Memulai Cursor pada Posisi Awal
            Toast.makeText(mContext, "anda memiliki cart", Toast.LENGTH_SHORT).show();
            while (res.moveToNext()) {

                Integer id = Integer.valueOf(res.getString(0));
                id_resto = res.getString(1);
                String id_menu = res.getString(2);
                String harga = (res.getString(3));
                Integer qty = Integer.valueOf(res.getString(4));
                String catatan = res.getString(5);
                String nama_resto = res.getString(6);

                CartList temp = new CartList(id, id_resto, id_menu, harga, qty, catatan, nama_resto, 0, "", 0);
                cartListTemp.add(temp);
            }
            get_restoran();


        }
    }


    //Button Resto on Click
    @OnClick(R.id.btnResto)
    void toResto() {
        progressResto = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        if (jmlCart == 0) {
            progressResto.dismiss();
            Toast.makeText(mContext, "Anda Tidak Memiliki Cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "" + id_resto, Toast.LENGTH_SHORT).show();
            mApiService.getRestoranByID(id_resto).enqueue(new Callback<ResponseOneRestoran>() {
                @Override
                public void onResponse(Call<ResponseOneRestoran> call, Response<ResponseOneRestoran> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getValue().equals("1")) {
                            Restoran restoran = response.body().getData();
                            progressResto.dismiss();
                            Intent intent = new Intent(mContext, MenuActivity.class);
                            intent.putExtra("Resto", restoran);
                            mContext.startActivity(intent);
                        } else {
                            progressResto.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneRestoran> call, Throwable t) {
                    progressResto.dismiss();

                }
            });
        }
    }

    @OnClick(R.id.btnBack)
    void Back() {
        onBackPressed();
    }

    @Override
    public void itemDeleted(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        if (cart.getDiscount().toString().isEmpty() || cart.getDiscount() == 0 || cart.getDiscount() == null) {
            SubTotal = SubTotal - (Float.parseFloat(cart.getHarga()) * cart.getQty());
        } else {
            SubTotal = SubTotal - (Float.parseFloat(String.valueOf(HitungDiscount(Double.parseDouble(cart.getHarga()), cart.getDiscount()))) * cart.getQty());
        }


        int msg = myDb.deleteCart(cart.getId());
        adapter.removeAt(position);
        //cartList.remove(position);
        SetData();

        if (cartList.size() == 0) {
            //set error untuk melihat pesan kosong
//            emtyFavorites.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            img_msg.setImageResource(R.drawable.msg_cart);
            title_msg.setText("Ayo Pesan Makanan Anda Sekarang");
            sub_title_msg.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesaan Anda \n");
            scCart.setVisibility(View.GONE);
        }
    }


    @Override
    public void itemMin(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        int qty = cart.getQty();
        if (qty > 1) {
            String id_menu = cart.getId_menu();
            qty -= 1;
            //cart.setQty(cart.getQty() - 1);
            // cartList.set(position, cart);
            boolean updete = myDb.UpdateCart(qty, id_menu);
            if (updete) {
                //cek diskon
                if (cart.getDiscount().toString().isEmpty() || cart.getDiscount() == 0 || cart.getDiscount() == null) {
                    SubTotal -= Float.parseFloat(cart.getHarga());
                } else {
                    SubTotal -= Float.parseFloat(String.valueOf(HitungDiscount(Double.parseDouble(cart.getHarga()), cart.getDiscount())));
                }
                SetData();
            }
        }

    }

    @Override
    public void itemPlus(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        int qty = cart.getQty();
        String id_menu = cart.getId_menu();
        qty += 1;
        // cart.setQty(cart.getQty() + 1);
        // cartList.set(position, cart);
        boolean updete = myDb.UpdateCart(qty, id_menu);
        if (updete) {
            //cek diskon
            if (cart.getDiscount().toString().isEmpty() || cart.getDiscount() == 0 || cart.getDiscount() == null) {
                SubTotal += Float.parseFloat(cart.getHarga());
            } else {
                SubTotal += Float.parseFloat(String.valueOf(HitungDiscount(Double.parseDouble(cart.getHarga()), cart.getDiscount())));
            }
            SetData();
        }

    }


    private double getBiayaAntar() {
        if (resto.getRestoranDelivery().equals("gratis")) {
            tempMsgTarifAntar = resto.getRestoranDelivery().toString();
            return 0;
        } else {
            tempMsgTarifAntar = resto.getRestoranDelivery() + " " + kursIndonesia(Double.parseDouble(resto.getRestoranDeliveryTarif()));
            return Double.valueOf(resto.getRestoranDeliveryTarif());
        }
    }

    //get restoran by id
    public void get_restoran() {
        mApiService.cekCart(location.get(SessionManager.LAT), location.get(SessionManager.LANG), id_resto, user.get(SessionManager.ID_USER).toString()).enqueue(new Callback<ResponseOneRestoran>() {
            @Override
            public void onResponse(Call<ResponseOneRestoran> call, Response<ResponseOneRestoran> response) {
                //    get restoran

                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        resto = response.body().getData();
                        menuList = response.body().getMenu();
                        saldo = Double.parseDouble(response.body().getKonsumenBalance());
                        setCartList();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "gagal dapat resto", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseOneRestoran> call, Throwable t) {
                progressDialog.dismiss();
                message.setVisibility(View.VISIBLE);
                img_msg.setImageResource(R.drawable.msg_no_connection);
                title_msg.setText("Opss.. Gagal terhubung kesever");
                sub_title_msg.setText("Priksa kembali koneksi internet Anda");
                scCart.setVisibility(View.GONE);

            }
        });
    }


    //set cart , update menu;
    public void setCartList() {

        for (int i = 0; i < cartListTemp.size(); i++) {
            CartList cart = cartListTemp.get(i);
            Boolean itemDelete = true;

            for (int j = 0; j < menuList.size(); j++) {

                Menu item = menuList.get(j);

                if (cart.getId_menu().equals(item.getId().toString())) {
                    itemDelete = false;

                    if (item.getMenuDiscount().toString().isEmpty() || item.getMenuDiscount() == 0 || item.getMenuDiscount() == null) {
                        //kondisi menu tidak discount
                        hargaFloat = Float.parseFloat(item.getMenuHarga());
                        SubTotal += (hargaFloat * cart.getQty());
                        Toast.makeText(mContext, "tidak terdeteksi diskon", Toast.LENGTH_SHORT).show();
                    } else {
                        hargaFloat = Float.parseFloat(String.valueOf(HitungDiscount(Double.parseDouble(item.getMenuHarga()), item.getMenuDiscount())));
                        SubTotal += hargaFloat * cart.getQty();
                        Toast.makeText(mContext, "terdeteksi diskon" + hargaFloat, Toast.LENGTH_SHORT).show();
                    }

                    //  hargaFloat = Float.parseFloat(item.getMenuHarga());
                    //  SubTotal += (hargaFloat * cart.getQty());


                    CartList temp = new CartList(cart.getId(), cart.getId_resto(), cart.getId_menu(), item.getMenuHarga(), cart.getQty(), cart.getCatatan(), item.getMenuNama(), item.getMenuDiscount(), item.getMenuFoto(), item.getMenuKetersediaan());
                    cartList.add(temp);
                }
            }
            if (itemDelete) {
                //hapus menu yang telah di delete
                int msg = myDb.deleteCart(cart.getId());
            }
        }
        if (cartList.isEmpty() || cartList.size() < 1) {
            emptyCart();
            progressDialog.dismiss();
        } else {

            SetData();
            adapter = new CartAdapter(CartListActivity.this, cartList, listener);
            list.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }


    //set data
    private void SetData() {
        Total = getBiayaAntar() + SubTotal;
        konsumen_id = user.get(SessionManager.ID_USER).toString();
        konsumen_nama = String.valueOf(user.get(SessionManager.NAMA_LENGKAP).toUpperCase());
        konsumen_phone = String.valueOf(user.get(SessionManager.NO_HP));
        mSubTotal.setText(kursIndonesia(SubTotal));
        mBiayaAntar.setText(tempMsgTarifAntar);
        mNamaKonsumen.setText(konsumen_nama);
        mPhoneKonsumen.setText("+" + konsumen_phone);

        //cek pajak
        if (resto.getRestoran_pajak_pb_satu() == 0) {
            viewpb1.setVisibility(View.GONE);
            lyt_pb1.setVisibility(View.GONE);
        } else {
            viewpb1.setVisibility(View.VISIBLE);
            lyt_pb1.setVisibility(View.VISIBLE);
            pajak_pb1.setText("PB1 (" + resto.getRestoran_pajak_pb_satu() + "%)");
            double pb1 = (resto.getRestoran_pajak_pb_satu() / 100.0) * Total;
            tvPajakPB1.setText(kursIndonesia(pb1));
            Total = Total + pb1;
        }

        mTotal.setText(kursIndonesia(Total));
        mAlamatAntar.setText(location.get(SessionManager.ALAMAT));

        restopayButton = (RadioButton) findViewById(R.id.rbRestoPay);
        RadioButton cashButton = (RadioButton) findViewById(R.id.rbCash);
        if (saldo >= Total) {
            restopayButton.setEnabled(true);
            mSaldo.setText("Saldo Anda " + kursIndonesia(saldo) + " Mencukupi");
            mSaldo.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            restopayButton.setEnabled(false);
            cashButton.setChecked(true);
            mSaldo.setText("Saldo Anda " + kursIndonesia(saldo) + " Tidak Mencukupi");
            mSaldo.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }

        checkKondisi();

    }

    //btn cttn click
    @OnClick(R.id.btnCatatan)
    void catatan() {
        mBtnCatatan.setVisibility(View.GONE);
        mCatatanAlamat.setVisibility(View.VISIBLE);
    }


    //Button order click
    @OnClick(R.id.btnOrder)
    void order() {
        Toast.makeText(mContext, "Sub total =" + String.valueOf(SubTotal) + "Total = " + String.valueOf(Total), Toast.LENGTH_SHORT).show();

        alertKonfirmasi();
    }

    //send order to restoran and insert database
    private void sendOrder() {
        int selectId = mMetodeBayar.getCheckedRadioButtonId();
        metodebayarButton = (RadioButton) findViewById(selectId);
        String metBayar = metodebayarButton.getText().toString();


        String title = konsumen_nama;
        String message = all_order();
        String id_konsumen = user.get(SessionManager.ID_USER).toString();
        final String id_restoran = id_resto;
        String pesan_lat = location.get(SessionManager.LAT);
        String pesan_long = location.get(SessionManager.LANG);
        String pesan_alamat = location.get(SessionManager.ALAMAT);
        String pesan_catatan = mCatatanAlamat.getText().toString();
        String jarak_antar = resto.getRestoranDistace().toString();
        String pesan_biaya_antar = Double.toString(getBiayaAntar());
        int pajakPb1 = resto.getRestoran_pajak_pb_satu();
        String pesan_metode_bayar;
        if (metBayar.equals("Resto - Pay ")) {
            pesan_metode_bayar = "epay";
        } else {
            pesan_metode_bayar = "cash";
        }


        ArrayList<String> menu = new ArrayList<String>();
        ArrayList<String> harga = new ArrayList<String>();
        ArrayList<String> qty = new ArrayList<String>();
        ArrayList<String> discount = new ArrayList<String>();
        ArrayList<String> catatan = new ArrayList<String>();

        for (int i = 0; i < cartList.size(); i++) {
            menu.add(cartList.get(i).getId_menu());
            harga.add(cartList.get(i).getHarga());
            qty.add(String.valueOf(cartList.get(i).getQty()));
            discount.add(String.valueOf(cartList.get(i).getDiscount()));
            catatan.add(cartList.get(i).getCatatan());
        }

        mApiService.createOrder(title, message, id_konsumen, id_restoran, pesan_lat, pesan_long, pesan_alamat, pesan_catatan, pesan_metode_bayar, jarak_antar, pesan_biaya_antar, pajakPb1, menu, qty, harga, discount, catatan).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressOrder.dismiss();
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    String id = response.body().getId();

                    if (value.equals("1")) {
                        Toast.makeText(mContext, "Berhasil Mengirim Pesanan ", Toast.LENGTH_SHORT).show();
                        //kosongkan cart dari db
                        myDb.deleteAll();
                        intentSucess(id);

                    } else if (value.equals("0")) {
                        Toast.makeText(mContext, (R.string.error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressOrder.dismiss();
                Toast.makeText(mContext, R.string.lostconnection, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }


    public void checkKondisi() {

        cekKetersedian();
        if (Double.parseDouble(resto.getRestoranDistace()) > resto.getRestoranDeliveryJarak()) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Lokasi Antar Diluar Jarak Pengantaran");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else if (SubTotal < Double.parseDouble(resto.getRestoranDeliveryMinimum())) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Pesanan Anda dibawah Minimum \n Minimum Subtotal " + kursIndonesia(Double.parseDouble(resto.getRestoranDeliveryMinimum())));
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else if (jmlMenuTidakTersedia > 0) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Terdapat Item yang Tidak Tersedia");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else {
            btnOrder.setEnabled(true);
            btnOrder.setText("Pesanan Sekarang");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));


        }
    }

    public void alertKonfirmasi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Konfirmasi Pesanan");
        builder.setMessage("Pesanan anda akan diterima " + resto.getRestoranNama().toString().toUpperCase() + " ,total pembayaran " + kursIndonesia(Total));
        builder.setCancelable(false);
        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressOrder = new ProgressDialog(mContext);
                progressOrder = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                sendOrder();
                // intentSucess();

            }
        });
        builder.setNegativeButton("Cek Kembali", new DialogInterface.OnClickListener() {
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


    //    untuk membuat list view anti scroll view
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


    //set mata unag format rupiah
    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    public String all_order() {
        String pesanan = "";
        String sperase = ",";
        for (int i = 0; i < cartList.size(); i++) {
            if (i == cartList.size() - 1) {
                sperase = ".";
            }
            pesanan += cartList.get(i).getNama_menu() + " " + cartList.get(i).getQty() + sperase;
        }
        return pesanan;
    }

    //fungsi menghitung discount
    public Double HitungDiscount(Double Harga, Integer Discount) {
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }

    //tampilkan jika tidak ada cart list
    private void emptyCart() {
        message.setVisibility(View.VISIBLE);
        img_msg.setImageResource(R.drawable.msg_cart);
        title_msg.setText("Ayo Pesan Makanan Anda Sekarang");
        sub_title_msg.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesaan Anda \n");
        scCart.setVisibility(View.GONE);
    }


    public void cekKetersedian() {
        int x = 0;
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getKetersediaan() == 0) {
                x++;
            }
        }
        jmlMenuTidakTersedia = x;

    }

    public void intentSucess(String id) {
        Intent intent = new Intent(mContext, SuccessOrder.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

