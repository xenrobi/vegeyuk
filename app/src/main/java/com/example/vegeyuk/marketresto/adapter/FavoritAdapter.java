package com.example.vegeyuk.marketresto.adapter;

import android.content.Context;
import android.graphics.Paint;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.Favorit;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.MyViewHolder> {

    private List<Favorit> favoritList;
    private Context mContext;
    private ClickListener clickListener;
    String path;


    public FavoritAdapter(Context context, List<Favorit> favorits, ClickListener clickListener) {
        super();
        this.mContext = context;
        this.favoritList = favorits;
        this.clickListener = clickListener;
    }

    public void removeAt(int position) {
        favoritList.remove(position);
        notifyDataSetChanged();

    }


    @Override
    public FavoritAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_favorit, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        path = view.getResources().getString(R.string.path);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavoritAdapter.MyViewHolder holder, final int position) {
        final Favorit favorit = favoritList.get(position);
        final Menu menu = favorit.getFavoritMenu();
        final Restoran restoran = favorit.getFavoritRestoran();

        holder.mNamaMenu.setText(menu.getMenuNama());
        holder.mNamaResto.setText(restoran.getRestoranNama());
        holder.mHargaMenu.setText(menu.getMenuHarga());

        Picasso.get()
                .load(path + menu.getMenuFoto())
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageMenu);

        holder.mImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_zoom, null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(path + menu.getMenuFoto()).into(photoView);
                //photoView.setImageResource(R.drawable.nature);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        // harga dan discount
        if (menu.getMenuDiscount().toString().isEmpty() || menu.getMenuDiscount() == 0 || menu.getMenuDiscount() == null) {
            //kondisi menu tidak discount
            holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(menu.getMenuHarga())));
        } else {
            holder.layoutDiscount.setVisibility(View.VISIBLE);
            holder.mDiscount.setText("-" + menu.getMenuDiscount() + "%");
            holder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(menu.getMenuHarga())));
            holder.mHargaCoret.setPaintFlags(holder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mHargaMenu.setText(kursIndonesia(HitungDiscount(Double.parseDouble(menu.getMenuHarga()), menu.getMenuDiscount())));
        }

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.itemDeleted(view, position);
                }
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.itemClick(view, position);
                }
            }
        });

        oprasional(holder, restoran.getRestoranOprasional());

        ketersediaan(holder, menu.getMenuKetersediaan());

    }

    @Override
    public int getItemCount() {
        return favoritList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNamaMenu)
        TextView mNamaMenu;
        @BindView(R.id.tvNamaResto)
        TextView mNamaResto;
        @BindView(R.id.tvHargaMenu)
        TextView mHargaMenu;
        @BindView(R.id.imgDelete)
        ImageView mDelete;
        @BindView(R.id.layoutDiscount)
        LinearLayout layoutDiscount;
        @BindView(R.id.tvHargaCoret)
        TextView mHargaCoret;
        @BindView(R.id.tvDiscount)
        TextView mDiscount;
        @BindView(R.id.imageMenu)
        ImageView mImageMenu;
        @BindView(R.id.parentLayout)
        LinearLayout parentLayout;
        @BindView(R.id.tvKetersedian)
        TextView mKetersediaan;
        @BindView(R.id.tvOperasional)
        TextView mOperasional;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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

    public interface ClickListener {
        //public void itemClicked(View view, int position);

        public void itemDeleted(View view, int position);

        public void itemClick(View view, int position);
    }

    public void oprasional(FavoritAdapter.MyViewHolder holder, Integer code) {

        if (code == 1) {
            holder.mOperasional.setText("Buka");
            //holder.tvOptasional.setBackground(ContextCompat.getDrawable(mContext,R.drawable.rounded_corner_green));
            holder.mOperasional.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            holder.mOperasional.setText("Tutup");
            holder.mOperasional.setBackgroundResource(R.drawable.rounded_corner_red);
        }
    }

    public void ketersediaan(MyViewHolder holder, Integer code) {

        if (code == 1) {
            holder.mKetersediaan.setText("Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.mKetersediaan.setText("Tidak Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }
}
