package com.example.vegeyuk.marketresto.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.utils.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class DialogPlaceOrderFragment extends DialogFragment {

    public static final String ARG_ITEM_ID = "custom_dialog_fragment";
    Menu menuItems;
    TextView item_title, mDeskripsi, mJumlah, mTotal, mHarga, mTvHargaCoret, mDiscount;
    EditText mCatatanMenu;
    int qty;
    LinearLayout mLayoutDiscount;
    double harga, total_harga, harga_item;
    String id_restoran, id_menu, catatan, nama_menu;
    DatabaseHelper myDb;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        menuItems = (Menu) bundle.getSerializable("selectedItem");

        myDb = new DatabaseHelper(getActivity());

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //set the layout for the dialog
        dialog.setContentView(R.layout.dialog_place_order);
        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        item_title = (TextView) dialog.findViewById(R.id.item_title);
        mDeskripsi = (TextView) dialog.findViewById(R.id.item_description);
        mHarga = (TextView) dialog.findViewById(R.id.item_price);
        mJumlah = (TextView) dialog.findViewById(R.id.tvQty);
        mTotal = (TextView) dialog.findViewById(R.id.total_price);
        mCatatanMenu = (EditText) dialog.findViewById(R.id.catatanMenu);
        mTvHargaCoret = (TextView) dialog.findViewById(R.id.tvHargaCoret);
        mDiscount = (TextView) dialog.findViewById(R.id.tvDiscount);
        mLayoutDiscount = (LinearLayout) dialog.findViewById(R.id.layoutDiscount);


        qty = 1;

        if (menuItems.getMenuDiscount().toString().isEmpty() || menuItems.getMenuDiscount() == 0 || menuItems.getMenuDiscount() == null) {
            //kondisi menu tidak discount
            total_harga = Double.valueOf(menuItems.getMenuHarga());
            harga_item = Double.valueOf(menuItems.getMenuHarga());
        } else {

            total_harga = HitungDiscount(Double.parseDouble(menuItems.getMenuHarga()), menuItems.getMenuDiscount());
            harga_item = total_harga;
        }

        setData();

        mJumlah.setText(String.valueOf(qty));
        mTotal.setText(kursIndonesia(total_harga));


        dialog.findViewById(R.id.tvUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty++;
                total_harga += harga_item;
                mJumlah.setText(String.valueOf(qty));
                mTotal.setText(kursIndonesia(total_harga));
            }
        });

        dialog.findViewById(R.id.tvMin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty > 1) {
                    qty--;
                    total_harga -= harga_item;
                    mJumlah.setText(String.valueOf(qty));
                    mTotal.setText(kursIndonesia(total_harga));
                }
            }
        });


        dialog.findViewById(R.id.order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ambil Seluruh data di db
                Cursor res = myDb.getAllCart();
                //cek berisi atau tidak
                //jika tidak berisi
                if (res.getCount() == 0) {
                    // Toast.makeText(getActivity(),"jumlah "+ qty +",harga "+String.valueOf(total_harga) ,Toast.LENGTH_SHORT).show();
                    //Insert Data
                    insertData();
                    //jika bersisi
                } else {
                    res.moveToFirst();
                    //cek apakah dipilih dari restoran yang sama ?
                    //Jika dari restoran yang sama
                    if (res.getString(1).equals(id_restoran)) {
                        //Toast.makeText(getActivity(),"data sesuai restoran",Toast.LENGTH_SHORT).show();

                        //ambil di db apakah menu sudah ada
                        Cursor cekUpdate = myDb.getByID(id_menu);
                        //Toast.makeText(getActivity(),String.valueOf(cekUpdate.getCount()),Toast.LENGTH_SHORT).show();
                        //Jika belum Insert data
                        if (cekUpdate.getCount() == 0) {
                            insertData();
                            //Toast.makeText(getActivity(),"Data Masuk !jml data"+String.valueOf(cekUpdate.getCount())+"menu id"+ id_menu,Toast.LENGTH_SHORT).show();
                            //Jika Ada Update data
                        } else {
                            //Toast.makeText(getActivity(),"Data Update !jml data"+String.valueOf(cekUpdate.getCount())+"menu id"+ id_menu,Toast.LENGTH_SHORT).show();
                            cekUpdate.moveToFirst();
                            int qtySkg = Integer.valueOf(cekUpdate.getString(4));
                            int qtyBaru = qtySkg + qty;
                            boolean update = myDb.UpdateCart(qtyBaru, id_menu);
                            dismiss();
                        }
                        //Jika beda restoran
                    } else {
                        Toast.makeText(getActivity(), "Opps, Keranjang Hanya untuk satu restoran" + id_restoran, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // User cancelled the dialog
                dismiss();
            }

        });


        return dialog;

    }

    private void setData() {
        id_restoran = menuItems.getIdRestoran().toString();
        id_menu = menuItems.getId().toString();

        harga = Double.valueOf(menuItems.getMenuHarga());
        nama_menu = menuItems.getMenuNama().toString();


        item_title.setText(menuItems.getMenuNama().toString());
        mDeskripsi.setText(menuItems.getMenuDeskripsi().toString());

        // harga dan discount
        if (menuItems.getMenuDiscount().toString().isEmpty() || menuItems.getMenuDiscount() == 0 || menuItems.getMenuDiscount() == null) {
            //kondisi menu tidak discount
            mHarga.setText(kursIndonesia(Double.parseDouble(menuItems.getMenuHarga().toString())) + " /" + menuItems.getMenuSatuan());
        } else {
            mLayoutDiscount.setVisibility(View.VISIBLE);
            mDiscount.setText("Discount " + menuItems.getMenuDiscount() + "%");
            mTvHargaCoret.setText(kursIndonesia(Double.parseDouble(menuItems.getMenuHarga())));
            mTvHargaCoret.setPaintFlags(mTvHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mHarga.setText(kursIndonesia(HitungDiscount(Double.parseDouble(menuItems.getMenuHarga()), menuItems.getMenuDiscount())) + " /" + menuItems.getMenuSatuan());

        }


        mDeskripsi.setMovementMethod(new ScrollingMovementMethod());
    }

    public void insertData() {
        catatan = mCatatanMenu.getText().toString();
        boolean isInserted = myDb.insertDataCart(id_restoran, id_menu, harga, qty, catatan, nama_menu);
        if (isInserted = true) {
            Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Data not Inserted", Toast.LENGTH_LONG).show();
        }
        dismiss();
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }

    public Double HitungDiscount(Double Harga, Integer Discount) {
        int discount = Discount / 100;
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }


}
