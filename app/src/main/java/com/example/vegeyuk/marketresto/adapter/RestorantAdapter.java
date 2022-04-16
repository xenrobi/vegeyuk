package com.example.vegeyuk.marketresto.adapter;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.MenuActivity;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestorantAdapter extends RecyclerView.Adapter<RestorantAdapter.RestoranViewHolder> implements Filterable {

    private List<Restoran> mArrayList;
    private List<Restoran> mFilteredList;
    private Context mContext;
    SessionManager sessionManager;
    HashMap<String, String> loca;
    String strDistance;
    View view;


    public RestorantAdapter(Context context, List<Restoran> dataList) {
        this.mArrayList = dataList;
        this.mFilteredList = dataList;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        loca = sessionManager.getLocation();
    }

    @Override
    public RestoranViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_restoran, parent, false);
        RestoranViewHolder holder = new RestoranViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RestoranViewHolder holder, final int position) {
        final Restoran data = mFilteredList.get(position);
        holder.txtNamaResto.setText(data.getRestoranNama());
        if (data.getRestoranDelivery().equals("gratis")) {
            holder.txtTarifDelivery.setText("Gratis");
            holder.txtTarifDelivery.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.txtTarifDelivery.setText(kursIndonesia(Double.parseDouble(data.getRestoranDeliveryTarif())));
            holder.txtTarifDelivery.setTextColor(ContextCompat.getColor(mContext, R.color.textSub));
        }

        holder.txtMinimum.setText(kursIndonesia(Double.parseDouble(data.getRestoranDeliveryMinimum())));
        holder.tvJumlahPesan.setText(data.getRestoranOrder().toString() + " Pesanan");
        String Deskripsi = data.getRestoranDeskripsi().substring(0, 1).toUpperCase() + data.getRestoranDeskripsi().substring(1);
        holder.tvDeskripsi.setText(Deskripsi);
        holder.tvJarak.setText(satuan_jarak(data.getRestoranDistace()));

        oprasional(holder, data.getRestoranOprasional());

        Picasso.get()
                .load(view.getResources().getString(R.string.path_restoran) + data.getRestoranFoto())
                .resize(500, 500)
                .centerCrop()
                .into(holder.imgRestoran);


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Anda Memilih " + data.getRestoranNama(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MenuActivity.class);
                intent.putExtra("Resto", data);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Restoran> filteredList = new ArrayList<>();

                    for (Restoran restoranverdion : mArrayList) {

                        if (restoranverdion.getRestoranNama().toLowerCase().contains(charString)) {
                            filteredList.add(restoranverdion);
                        }

                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Restoran>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class RestoranViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNamaResto)
        TextView txtNamaResto;
        @BindView(R.id.tvTarif)
        TextView txtTarifDelivery;
        @BindView(R.id.tvMin)
        TextView txtMinimum;
        @BindView(R.id.parentLayout)
        LinearLayout mParentLayout;
        @BindView(R.id.tvJumlah_pesan)
        TextView tvJumlahPesan;
        @BindView(R.id.tvOprasional)
        TextView tvOptasional;
        @BindView(R.id.tvDeskripsi_resto)
        TextView tvDeskripsi;
        @BindView(R.id.tvJarak)
        TextView tvJarak;
        @BindView(R.id.imageRestoran)
        ImageView imgRestoran;


        public RestoranViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    public void oprasional(RestoranViewHolder holder, Integer code) {

        if (code == 1) {
            holder.tvOptasional.setText("Buka");
            //holder.tvOptasional.setBackground(ContextCompat.getDrawable(mContext,R.drawable.rounded_corner_green));
            holder.tvOptasional.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            holder.tvOptasional.setText("Tutup");
            holder.tvOptasional.setBackgroundResource(R.drawable.rounded_corner_red);
        }
    }

    public void hitung_jarak(String lokasi_resto) {
        String[] locResto = lokasi_resto.split(",");
        double lat1 = Double.parseDouble(loca.get(SessionManager.LAT));
        double lng1 = Double.parseDouble(loca.get(SessionManager.LANG));
        double lat2 = Double.parseDouble(locResto[0]);
        double lng2 = Double.parseDouble(locResto[1]);
        Location asal = new Location("point A");
        asal.setLatitude(lat1);
        asal.setLongitude(lng1);
        Location tujuan = new Location("point B");
        tujuan.setLatitude(lat2);
        tujuan.setLongitude(lng2);

        double distance = (double) Math.floor(asal.distanceTo(tujuan) / 1000 * 100) / 100;

        if (distance < 1) {
            strDistance = String.valueOf(distance * 1000) + " m";
        } else {
            strDistance = String.valueOf(distance) + " km";
        }


    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    private String satuan_jarak(String jarak) {
        String jarakStr = null;
        if (Double.parseDouble(jarak) < 1) {
            jarakStr = Double.parseDouble(jarak) * 1000 + " m";
        } else {
            jarakStr = jarak + " Km";
        }
        return jarakStr;
    }


}
