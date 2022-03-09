package com.example.vegeyuk.marketresto.rest;

import com.example.vegeyuk.marketresto.responses.ResponseAuth;
import com.example.vegeyuk.marketresto.responses.ResponseFavorit;
import com.example.vegeyuk.marketresto.responses.ResponseKonsumen;
import com.example.vegeyuk.marketresto.responses.ResponseMenu;
import com.example.vegeyuk.marketresto.responses.ResponseOneRestoran;
import com.example.vegeyuk.marketresto.responses.ResponseOrder;
import com.example.vegeyuk.marketresto.responses.ResponseRestoran;
import com.example.vegeyuk.marketresto.responses.ResponseSearch;
import com.example.vegeyuk.marketresto.responses.ResponseValue;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //fungsi ini untuk memanggil API localhost:8080/


    // Fungsi ini untuk memanggil API 127.0.0.1/auth/signup
    @FormUrlEncoded
    @POST("konsumen")
    Call<ResponseValue> signupRequest(@Field("konsumen_nama") String nama,
                                      @Field("konsumen_phone") String phone,
                                      @Field("konsumen_email") String email,
                                      @Field("token") String token);


    // Fungsi ini untuk memanggil API 127.0.01/auth/signin
    //login
    @GET("marketresto/login/{phone}")
    Call<ResponseAuth> signinRequest(@Path("phone") String phone);

    //Update Token if Logged
    @FormUrlEncoded
    @PATCH("konsumen/{konsuman}")
    Call<ResponseValue> updateToken(@Path("konsuman") String id,
                                    @Field("token") String token);


    //Get List Restoran
    @GET("restoran")
    Call<ResponseRestoran> getRestoran(@Query("lat") String lat,
                                       @Query("long") String lang);

    //    get one restoran
    @GET("restoran/{restoran}")
    Call<ResponseOneRestoran> getRestoranByID(@Path("restoran") String id_restoran);

    //get menu from restoran
    @GET("menu")
    Call<ResponseMenu> getRestoranMenu(@Query("id") String id_restoran,
                                       @Query("id_konsumen") String id_konsumen);


    //cek cart
    @GET("restoran")
    Call<ResponseOneRestoran> cekCart(@Query("lat") String lat,
                                      @Query("long") String lang,
                                      @Query("id_restoran") String id_restoran,
                                      @Query("id_konsumen") String id_konsumen);


    @FormUrlEncoded
    @POST("order")
    Call<ResponseValue> createOrder(@Field("title") String title,
                                    @Field("message") String message,
                                    @Field("id_konsumen") String id_konsumen,
                                    @Field("id_restoran") String id_restoran,
                                    @Field("order_lat") String order_lat,
                                    @Field("order_long") String order_long,
                                    @Field("order_alamat") String order_alamat,
                                    @Field("order_catatan") String order_catatan,
                                    @Field("order_metode_bayar") String order_metode_bayar,
                                    @Field("order_jarak_antar") String order_jarak_antar,
                                    @Field("order_biaya_anatar") String order_biaya_anatar,
                                    @Field("order_pajak_pb_satu") Integer order_pajak_pb_satu,
                                    @Field("menu[]") ArrayList<String> menu,
                                    @Field("qty[]") ArrayList<String> qty,
                                    @Field("harga[]") ArrayList<String> harga,
                                    @Field("discount[]") ArrayList<String> discount,
                                    @Field("catatan[]") ArrayList<String> catatan);

    //gerOrderIn proces
    @GET("order")
    Call<ResponseOrder> getOrder(@Query("id_konsumen") String id_konsumen,
                                 @Query("status[]") ArrayList<String> status);

    //setFavorit
    @FormUrlEncoded
    @POST("favorit")
    Call<ResponseValue> setFavorit(@Field("id_konsumen") String id_konsumen,
                                   @Field("id_menu") String id_menu);

    // getFavorit
    @GET("favorit/{favorit}")
    Call<ResponseFavorit> getFavorit(@Path("favorit") String id_konsumen);

    // getFavorit
    @DELETE("favorit/{favorit}")
    Call<ResponseValue> deleteFavorit(@Path("favorit") Integer id_konsumen);


    //cek cart
    @GET("restoran")
    Call<ResponseSearch> cari(@Query("lat") String lat,
                              @Query("long") String lang,
                              @Query("cari") String cari
    );

    @GET("konsumen/{konsuman}")
    Call<ResponseKonsumen> getKonsumen(@Path("konsuman") String id);


    //Update Konsumen
    @FormUrlEncoded
    @PATCH("konsumen/{konsuman}")
    Call<ResponseValue> updateKonsumen(@Path("konsuman") String id,
                                       @Field("konsumen_nama") String nama,
                                       @Field("konsumen_phone") String phone,
                                       @Field("konsumen_email") String email,
                                       @Field("token") String token);


    //top up
    @FormUrlEncoded
    @POST("order/struk")
    Call<ResponseValue> sendEmail(@Field("id_order") Integer id_order);


    //rekomendasi
    @FormUrlEncoded
    @POST("rekomendasi")
    Call<ResponseValue> rekomendasi(@Field("nama_restoran") String nama_restoran,
                                    @Field("number_phone") String number_phone,
                                    @Field("alamat") String alamat);


    //uat
    @FormUrlEncoded
    @POST("uat")
    Call<ResponseValue> uat(@Field("id_user") String id_user,
                            @Field("soal_1") int soal_1,
                            @Field("soal_2") int soal_2,
                            @Field("soal_3") int soal_3,
                            @Field("soal_4") int soal_4,
                            @Field("soal_5") int soal_5,
                            @Field("soal_6") int soal_6);


//    old

//    //    get restoran
//    @GET("restoran/")
//    Call<ResponseRestoran> getRestoran ();

//    //    get restoran
//    @GET("restoran/{id_restoran}")
//    Call<ResponseRestoran> getRestoranByID (@Path("id_restoran") String id_restoran);

    //    get restoran menu
//    @GET("menu/{id_restoran}")
//    Call<ResponseMenu> getRestoranMenuById (@Path("id_restoran") String id_restoran,
//                                            @Query("id_konsumen") String id_konsumen);
//

    // OrderPesanan
//    @FormUrlEncoded
//    @POST("send/")
//    Call<ResponseSend> order(@Field("title") String title,
//                             @Field("message") String message,
//                             @Field("restoran_phone") String restoran_phone,
//                             @Field("konsumen_id") String konsumen_id,
//                             @Field("restoran_id") String restoran_id,
//                             @Field("pesan_lokasi") String pesan_lokasi,
//                             @Field("pesan_alamat")String pesan_alamat,
//                             @Field("pesan_catatan") String pesan_catatan,
//                             @Field("jarak_antar") String jarak_antar,
//                             @Field("pesan_biaya_antar")String pesan_biaya_antar,
//                             @Field("pesan_status")String pesan_status,
//                             @Field("pesan_metode_bayar") String pesan_metode_bayar);
//
//    @FormUrlEncoded
//    @POST("pesandetail/")
//    Call<ResponseBody> setPesanDetail (@Field("pesan_id") String pesan_id,
//                                       @Field("menu_id") String menu_id,
//                                       @Field("harga") String harga,
//                                       @Field("qty") String qty,
//                                       @Field("catatan") String catatan);
//

    //gerOrderIn proces
//    @GET("order/konsumen/{id_konsumen}")
//    Call<ResponseOrder> getOrderProces (@Path ("id_konsumen") String id_konsumen);

//    //update token
//    @FormUrlEncoded
//    @PUT("auth/token/{id_pengguna}")
//    Call<ResponseAuth> updateToken (@Path("id_pengguna") String id_pengguna,
//                                            @Field("token") String token);


}
