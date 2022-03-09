package com.example.vegeyuk.marketresto.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.Menu;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailOrderAdapter extends BaseAdapter {

    Context mContext;
    List<Menu> detailOrders;
    ViewHolder viewHolder;


    public DetailOrderAdapter(Context context, List<Menu> data) {
        super();
        this.mContext = context;
        this.detailOrders = data;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.single_row_list_detail_order, null);

            viewHolder = new ViewHolder();

            viewHolder.mNamaMenu = (TextView) view.findViewById(R.id.tvNamaMenu);
            viewHolder.mQty = (TextView) view.findViewById(R.id.qty);
            viewHolder.catatan = (TextView) view.findViewById(R.id.catatanMenu);
            viewHolder.mHarga = (TextView) view.findViewById(R.id.tvHargaMenu);
            viewHolder.mJml = (TextView) view.findViewById(R.id.tvHargaTotal);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            viewHolder.layoutDiscount = (LinearLayout) view.findViewById(R.id.layoutDiscount);
            viewHolder.mHargaCoret = (TextView) view.findViewById(R.id.tvHargaCoret);
            viewHolder.mDiscount = (TextView) view.findViewById(R.id.tvDiscount);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        final Menu order = (Menu) getItem(position);
        viewHolder.mNamaMenu.setText(order.getMenuNama());
        viewHolder.mQty.setText(String.valueOf(order.getPivot().getQty()));
//        viewHolder.qty.setText(String .valueOf(cart.getQty()));
        Double jml = null;
        if (order.getPivot().getDiscount() == 0 || order.getPivot().getDiscount().toString().isEmpty()) {
            viewHolder.layoutDiscount.setVisibility(View.GONE);
            viewHolder.mHarga.setText("@" + kursIndonesia(Double.parseDouble(order.getPivot().getHarga())));
            jml = order.getPivot().getQty() * Double.parseDouble(order.getPivot().getHarga());
        } else {
            viewHolder.layoutDiscount.setVisibility(View.VISIBLE);
            viewHolder.mDiscount.setText("-" + order.getPivot().getDiscount() + "%");
            viewHolder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(order.getPivot().getHarga())));
            viewHolder.mHargaCoret.setPaintFlags(viewHolder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Double harga_discount = HitungDiscount(Double.parseDouble(order.getPivot().getHarga()), order.getPivot().getDiscount());
            viewHolder.mHarga.setText("@" + kursIndonesia(harga_discount));
            jml = order.getPivot().getQty() * harga_discount;
        }

        viewHolder.mJml.setText(kursIndonesia(jml));


        if (order.getPivot().getCatatan() == null) {
            viewHolder.catatan.setVisibility(View.GONE);
        } else {
            viewHolder.catatan.setVisibility(View.VISIBLE);
            viewHolder.catatan.setText(order.getPivot().getCatatan());
        }

        String path = view.getResources().getString(R.string.path) + order.getMenuFoto();
        Picasso.get()
                .load(path)
                .resize(500, 500)
                .centerCrop()
                .into(viewHolder.imageView);


        return view;
    }

    @Override
    public int getCount() {
        return detailOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return detailOrders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView mNamaMenu;
        TextView mQty;
        TextView mHarga;
        TextView mJml;
        TextView catatan;
        LinearLayout layoutDiscount;
        TextView mHargaCoret;
        TextView mDiscount;

    }

    public Double HitungDiscount(Double Harga, Integer Discount) {
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }

}
