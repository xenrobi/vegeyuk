package com.example.vegeyuk.marketresto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorit {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_konsumen")
    @Expose
    private Integer idKonsumen;
    @SerializedName("id_menu")
    @Expose
    private Integer idMenu;
    @SerializedName("favorit_menu")
    @Expose
    private Menu favoritMenu;
    @SerializedName("favorit_restoran")
    @Expose
    private Restoran favoritRestoran;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdKonsumen() {
        return idKonsumen;
    }

    public void setIdKonsumen(Integer idKonsumen) {
        this.idKonsumen = idKonsumen;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public Menu getFavoritMenu() {
        return favoritMenu;
    }

    public void setFavoritMenu(Menu favoritMenu) {
        this.favoritMenu = favoritMenu;
    }

    public Restoran getFavoritRestoran() {
        return favoritRestoran;
    }

    public void setFavoritRestoran(Restoran favoritRestoran) {
        this.favoritRestoran = favoritRestoran;
    }

}
