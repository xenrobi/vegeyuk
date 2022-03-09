package com.example.vegeyuk.marketresto.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import com.example.vegeyuk.marketresto.activities.MenuActivity;
import com.example.vegeyuk.marketresto.config.ServerConfig;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.example.vegeyuk.marketresto.responses.ResponseOneRestoran;
import com.example.vegeyuk.marketresto.rest.ApiService;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSearchAdapter extends RecyclerView.Adapter<MenuSearchAdapter.MyViewHolder> {

    private List<Menu> menuList;

    private Context mContext;
    FragmentManager fragmentManager;
    ApiService mApiService;
    View view;


    public MenuSearchAdapter(Context mContext, List<Menu> data) {
        super();
        this.menuList = data;

        this.mContext = mContext;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        mApiService = ServerConfig.getAPIService();
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Menu data = menuList.get(position);
        holder.mNamaMenu.setText(data.getMenuNama());
        final String path = view.getResources().getString(R.string.path);


        Picasso.get()
                .load(view.getResources().getString(R.string.path) + data.getMenuFoto())
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


        oprasional(holder, data.getMenuKetersediaan());


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

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //           Toast.makeText(mContext,"you click "+ data.getMenuNama(),Toast.LENGTH_SHORT).show();

                if (data.getMenuKetersediaan() == 1) {

                    getResto(data.getIdRestoran().toString(), data.getId().toString());


                } else {
                    Toast.makeText(mContext, data.getMenuNama() + " Tidak Tesedia", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void getResto(final String id, final String id_menu) {
        mApiService.getRestoranByID(id).enqueue(new Callback<ResponseOneRestoran>() {
            @Override
            public void onResponse(Call<ResponseOneRestoran> call, Response<ResponseOneRestoran> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equalsIgnoreCase("1")) {
                        Restoran data = response.body().getData();
                        Intent intent = new Intent(mContext, MenuActivity.class);
                        intent.putExtra("Resto", data);
                        mContext.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOneRestoran> call, Throwable t) {

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


}


