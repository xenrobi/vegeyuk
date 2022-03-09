package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.Kategori;
import com.example.vegeyuk.marketresto.models.Menu;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMenu {


    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("restoran_menu")
    @Expose
    private List<Menu> restoranMenu = null;
    @SerializedName("restoran_kategori")
    @Expose
    private List<Kategori> restoranKategori = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Menu> getRestoranMenu() {
        return restoranMenu;
    }

    public void setRestoranMenu(List<Menu> restoranMenu) {
        this.restoranMenu = restoranMenu;
    }

    public List<Kategori> getRestoranKategori() {
        return restoranKategori;
    }

    public void setRestoranKategori(List<Kategori> restoranKategori) {
        this.restoranKategori = restoranKategori;

    }
}
