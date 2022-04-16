package com.example.vegeyuk.marketresto.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.DetailOrderActivity;
import com.example.vegeyuk.marketresto.activities.MapsDeliveryActivity;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Order;
import com.example.vegeyuk.marketresto.utils.DateHelper;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context mContext;
    private List<Order> pesanList;
    private List<Menu> detailpesanList;

    public OrderAdapter(Context ctx, List<Order> pesanList) {
        this.pesanList = pesanList;
        this.mContext = ctx;
    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_order, parent, false);
        OrderAdapter.OrderViewHolder holder = new OrderAdapter.OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final Order pesan = pesanList.get(position);
        holder.mNoOrder.setText("Order :" + " #" + pesan.getId());
        holder.mNamaResto.setText(pesan.getOrderRestoran());
        // String status = pesan.getPesanStatus().substring(0,1).toUpperCase() + pesan.getPesanStatus().substring(1).toLowerCase();
        holder.mStatus.setText(pesan.getOrderStatus());
        holder.mTanggal.setText(DateHelper.getGridDate(mContext, timeStringToMilis(pesan.getCreatedAt())));

        if (pesan.getOrderStatus().equalsIgnoreCase("proeses")) {
            holder.mapDelivery.setVisibility(View.INVISIBLE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("pengantaran")) {
            holder.mapDelivery.setVisibility(View.VISIBLE);
            holder.mapDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MapsDeliveryActivity.class);
                    intent.putExtra("pesan", (Serializable) pesan);
                    mContext.startActivity(intent);
                }
            });
        }

        detailpesanList = pesan.getDetailOrder();
        double total = 0;
        for (int i = 0; i < detailpesanList.size(); i++) {
            total += Double.parseDouble(detailpesanList.get(i).getPivot().getHarga()) * detailpesanList.get(i).getPivot().getQty();
        }

        holder.mTotal.setText(kursIndonesia(total + Double.parseDouble(pesan.getOrderBiayaAnatar())));

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Anda memilih " + pesan.getOrderKonsumen(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailOrderActivity.class);
                intent.putExtra("pesan", (Serializable) pesan);

                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pesanList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNamaResto)
        TextView mNamaResto;
        @BindView(R.id.tvStatus)
        TextView mStatus;
        @BindView(R.id.tvWaktu)
        TextView mTanggal;
        @BindView(R.id.parentLayout)
        LinearLayout mParentLayout;
        @BindView(R.id.tvTotal)
        TextView mTotal;
        @BindView(R.id.tvNoOrder)
        TextView mNoOrder;
        @BindView(R.id.opendelivery)
        ImageView mapDelivery;


        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //convert time string to milis date
    public long timeStringToMilis(String strDate) {

        SimpleDateFormat tgl = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        long milliseconds = 0;
        Date date = null;
        try {
            date = tgl.parse(strDate);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


}
