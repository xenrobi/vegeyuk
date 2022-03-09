package com.example.vegeyuk.marketresto.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.models.Menu;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private List<Menu> menuList;
    private Context mContext;
    FragmentManager fragmentManager;
    private OnItemClickListener listener;
    String path;
    MyViewHolder holder2;


    public MenuAdapter(Context mContext, List<Menu> data, OnItemClickListener listener) {
        super();
        this.menuList = data;
        this.mContext = mContext;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        path = view.getResources().getString(R.string.path);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder2 = holder;
        final Menu data = menuList.get(position);
        holder.mNamaMenu.setText(data.getMenuNama());


        Picasso.get()
                .load(path + data.getMenuFoto())
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageMenu);

        holder.mImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_zoom, null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(path + data.getMenuFoto()).into(photoView);
                //photoView.setImageResource(R.drawable.nature);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        jumlahFavorit(data.getMenuJumlahFavorit(), holder);


        if (data.getMenuFavorit() > 0) {
            holder.mLove.setImageResource(R.drawable.f4);
        } else {
            holder.mLove.setImageResource(R.drawable.f0);
        }


        oprasional(holder, data.getMenuKetersediaan());


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //           Toast.makeText(mContext,"you click "+ data.getMenuNama(),Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    //Menu Tersedia
                    if (data.getMenuKetersediaan() == 1) {
                        listener.onItemCliked(view, position, false);
                    } else {
                        Toast.makeText(mContext, data.getMenuNama() + " Tidak Tesedia", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // harga dan discount
        if (data.getMenuDiscount().toString().isEmpty() || data.getMenuDiscount() == 0 || data.getMenuDiscount() == null) {
            //kondisi menu tidak discount
            holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getMenuHarga())));
        } else {
            holder.layoutDiscount.setVisibility(View.VISIBLE);
            holder.mDiscount.setText("-" + data.getMenuDiscount() + "%");
            holder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(data.getMenuHarga())));
            holder.mHargaCoret.setPaintFlags(holder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mHargaMenu.setText(kursIndonesia(HitungDiscount(Double.parseDouble(data.getMenuHarga()), data.getMenuDiscount())));

        }

        //long click
        holder.mParentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onItemCliked(view, position, true);

                }

                return true;
            }
        });
    }

    private void jumlahFavorit(Integer menuJumlahFavorit, MyViewHolder holder) {
        if (menuJumlahFavorit > 0) {
            holder.mJmlFavorit.setText(menuJumlahFavorit.toString());
        } else {
            holder.mJmlFavorit.setVisibility(View.INVISIBLE);
            holder.mLoveBlack.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNamaMenu)
        TextView mNamaMenu;
        @BindView(R.id.tvHargaMenu)
        TextView mHargaMenu;
        @BindView(R.id.parentLayout)
        CardView mParentLayout;
        @BindView(R.id.tvKetersedian)
        TextView mKetersediaan;
        @BindView(R.id.imgLove)
        ImageView mLove;
        @BindView(R.id.imageMenu)
        ImageView mImageMenu;
        @BindView(R.id.tvJmlFavorit)
        TextView mJmlFavorit;
        @BindView(R.id.imgLoveBlack)
        ImageView mLoveBlack;
        @BindView(R.id.tvHargaCoret)
        TextView mHargaCoret;
        @BindView(R.id.tvDiscount)
        TextView mDiscount;
        @BindView(R.id.layoutDiscount)
        LinearLayout layoutDiscount;

        public MyViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemCliked(View v, int position, boolean isLongClick);


    }

    public void oprasional(MyViewHolder holder, Integer code) {

        if (code == 1) {
            holder.mKetersediaan.setText("Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.mKetersediaan.setText("Tidak Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
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

    public void favoritAt(View view, int position) {
        MyViewHolder holder = new MyViewHolder(view);
        Menu menu = menuList.get(position);
        if (menu.getMenuFavorit() == 0) {
            Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
            menu.setMenuJumlahFavorit(menu.getMenuJumlahFavorit() + 1);
            menu.setMenuFavorit(1);
            menuList.set(position, menu);
            holder.mLoveBlack.setVisibility(View.VISIBLE);
            holder.mJmlFavorit.setVisibility(View.VISIBLE);
            holder.mJmlFavorit.setText(menu.getMenuJumlahFavorit().toString());
            // notifyDataSetChanged();
        }

    }


}


