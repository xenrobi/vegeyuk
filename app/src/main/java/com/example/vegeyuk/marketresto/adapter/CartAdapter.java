package com.example.vegeyuk.marketresto.adapter;

import android.app.Activity;
import android.content.Context;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.CartList;
import com.example.vegeyuk.marketresto.utils.DatabaseHelper;
import com.squareup.picasso.Picasso;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {

    Context mContext;
    List<CartList> dataList;
    DatabaseHelper myDb;
    private ClickListener clickListener;
    ViewHolder viewHolder = null;

    public CartAdapter(Context mContext, List<CartList> data, ClickListener clickListener) {
        super();
        this.dataList = data;
        this.mContext = mContext;
        this.clickListener = clickListener;

    }

    public void removeAt(int position) {
        dataList.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.single_row_cart_list, null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.harga = (TextView) view.findViewById(R.id.harga);
            viewHolder.qty = (TextView) view.findViewById(R.id.qty);
            viewHolder.min = (ImageView) view.findViewById(R.id.min);
            viewHolder.plus = (ImageView) view.findViewById(R.id.plus);
            viewHolder.cross = (ImageView) view.findViewById(R.id.clear);
            viewHolder.layoutDiscount = (LinearLayout) view.findViewById(R.id.layoutDiscount);
            viewHolder.mHargaCoret = (TextView) view.findViewById(R.id.tvHargaCoret);
            viewHolder.mDiscount = (TextView) view.findViewById(R.id.tvDiscount);
            viewHolder.mCatatanMenu = (EditText) view.findViewById(R.id.catatanMenu);
            viewHolder.mKetersedian = (TextView) view.findViewById(R.id.tvMenuKetersedian);


            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        CartList cart = (CartList) dataList.get(position);

        String path = view.getResources().getString(R.string.path) + cart.getMenu_foto();
        Picasso.get()
                .load(path)
                .resize(500, 500)
                .centerCrop()
                .into(viewHolder.imageView);

        // viewHolder.imageView.setImageResource(R.drawable.shoppy_logo);
        viewHolder.title.setText(cart.getNama_menu());


        if (cart.getDiscount() > 0) {
            //kondisi menu tidak discount
            viewHolder.layoutDiscount.setVisibility(View.VISIBLE);

            viewHolder.mDiscount.setText("-" + cart.getDiscount() + "%");
            viewHolder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(cart.getHarga())));
            viewHolder.mHargaCoret.setPaintFlags(viewHolder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.harga.setText(kursIndonesia(HitungDiscount(Double.parseDouble(cart.getHarga()), cart.getDiscount())));


        } else if (cart.getDiscount() == 0) {
            viewHolder.layoutDiscount.setVisibility(View.INVISIBLE);
            viewHolder.harga.setText(kursIndonesia(Double.parseDouble(cart.getHarga())));
        }


        viewHolder.qty.setText(String.valueOf(cart.getQty()));
        viewHolder.mCatatanMenu.setText(cart.getCatatan().toString());

        if (cart.getKetersediaan() == 0) {
            viewHolder.mKetersedian.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mKetersedian.setVisibility(View.GONE);
        }


        viewHolder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.itemMin(view, position);
                    CartList cl = dataList.get(position);
                    if (cl.getQty() > 1) {
                        cl.setQty(cl.getQty() - 1);
                        dataList.set(position, cl);
                        viewHolder.qty.setText(String.valueOf(cl.getQty()));
                        CartAdapter.this.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "Opps, Pesanan sudah minimum", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.itemPlus(view, position);
                    CartList cl = dataList.get(position);
                    cl.setQty(cl.getQty() + 1); //incerment item
                    dataList.set(position, cl); //updeting the item list to that new item with incremend quality
                    viewHolder.qty.setText(String.valueOf(cl.getQty()));
                    CartAdapter.this.notifyDataSetChanged();
                }
            }
        });

        viewHolder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.itemDeleted(view, position);
                }
            }
        });

        return view;


    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView harga;
        TextView qty;
        ImageView min;
        ImageView plus;
        ImageView cross;
        LinearLayout layoutDiscount;
        TextView mHargaCoret;
        TextView mDiscount;
        EditText mCatatanMenu;
        TextView mKetersedian;

    }


    public interface ClickListener {
        //public void itemClicked(View view, int position);

        public void itemDeleted(View view, int position);

        public void itemPlus(View view, int position);

        public void itemMin(View view, int position);

    }

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


}
