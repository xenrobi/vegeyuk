package com.example.vegeyuk.marketresto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kategori implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("kategori_nama")
    @Expose
    private String kategoriNama;
    @SerializedName("kategori_deskripsi")
    @Expose
    private String kategoriDeskripsi;
    @SerializedName("total_menu")
    @Expose
    private Integer totalMenu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKategoriNama() {
        return kategoriNama;
    }

    public void setKategoriNama(String kategoriNama) {
        this.kategoriNama = kategoriNama;
    }

    public String getKategoriDeskripsi() {
        return kategoriDeskripsi;
    }

    public void setKategoriDeskripsi(String kategoriDeskripsi) {
        this.kategoriDeskripsi = kategoriDeskripsi;
    }

    public Integer getTotalMenu() {
        return totalMenu;
    }

    public void setTotalMenu(Integer totalMenu) {
        this.totalMenu = totalMenu;
    }
}
