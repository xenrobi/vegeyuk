package com.example.vegeyuk.marketresto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_restoran")
    @Expose
    private Integer idRestoran;
    @SerializedName("id_kategori")
    @Expose
    private Integer idKategori;
    @SerializedName("menu_nama")
    @Expose
    private String menuNama;
    @SerializedName("menu_satuan")
    @Expose
    private String menuSatuan;
    @SerializedName("menu_foto")
    @Expose
    private String menuFoto;
    @SerializedName("menu_harga")
    @Expose
    private String menuHarga;
    @SerializedName("menu_deskripsi")
    @Expose
    private String menuDeskripsi;
    @SerializedName("menu_ketersediaan")
    @Expose
    private Integer menuKetersediaan;
    @SerializedName("menu_discount")
    @Expose
    private Integer menuDiscount;
    @SerializedName("menu_jumlah_favorit")
    @Expose
    private Integer menuJumlahFavorit;
    @SerializedName("menu_favorit")
    @Expose
    private Integer menuFavorit;
    @SerializedName("pivot")
    @Expose
    private DetailOrder pivot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRestoran() {
        return idRestoran;
    }

    public void setIdRestoran(Integer idRestoran) {
        this.idRestoran = idRestoran;
    }

    public Integer getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(Integer idKategori) {
        this.idKategori = idKategori;
    }

    public String getMenuNama() {
        return menuNama;
    }

    public void setMenuNama(String menuNama) {
        this.menuNama = menuNama;
    }

    public String getMenuFoto() {
        return menuFoto;
    }

    public void setMenuFoto(String menuFoto) {
        this.menuFoto = menuFoto;
    }

    public String getMenuHarga() {
        return menuHarga;
    }

    public void setMenuHarga(String menuHarga) {
        this.menuHarga = menuHarga;
    }

    public String getMenuDeskripsi() {
        return menuDeskripsi;
    }

    public void setMenuDeskripsi(String menuDeskripsi) {
        this.menuDeskripsi = menuDeskripsi;
    }

    public Integer getMenuKetersediaan() {
        return menuKetersediaan;
    }

    public void setMenuKetersediaan(Integer menuKetersediaan) {
        this.menuKetersediaan = menuKetersediaan;
    }

    public Integer getMenuDiscount() {
        return menuDiscount;
    }

    public void setMenuDiscount(Integer menuDiscount) {
        this.menuDiscount = menuDiscount;
    }

    public Integer getMenuJumlahFavorit() {
        return menuJumlahFavorit;
    }

    public void setMenuJumlahFavorit(Integer menuJumlahFavorit) {
        this.menuJumlahFavorit = menuJumlahFavorit;
    }

    public Integer getMenuFavorit() {
        return menuFavorit;
    }

    public void setMenuFavorit(Integer menuFavorit) {
        this.menuFavorit = menuFavorit;
    }


    public DetailOrder getPivot() {
        return pivot;
    }

    public void setPivot(DetailOrder pivot) {
        this.pivot = pivot;
    }

    public String getMenuSatuan() {
        return menuSatuan;
    }

    public void setMenuSatuan(String menuSatuan) {
        this.menuSatuan = menuSatuan;
    }
}
