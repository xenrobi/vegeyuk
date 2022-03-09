package com.example.vegeyuk.marketresto.models;

public class CartList {

    //private int image;
    private Integer id;
    private String id_resto;
    private String id_menu;
    private String harga;
    private Integer qty;
    private String catatan;
    private Integer discount;
    private String nama_menu;
    private String menu_foto;
    private int ketersediaan;


    public CartList(int id, String id_resto, String id_menu, String harga, Integer qty, String catatan, String nama_menu, Integer discount, String menu_foto, int ketersediaan) {
        this.id = id;
        this.id_resto = id_resto;
        this.id_menu = id_menu;
        this.harga = harga;
        this.qty = qty;
        this.catatan = catatan;
        this.nama_menu = nama_menu;
        this.discount = discount;
        this.menu_foto = menu_foto;
        this.ketersediaan = ketersediaan;
    }

//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }


    public Integer getId() {
        return id;
    }

    public void setPrice(Integer id) {
        this.id = id;
    }


    public String getId_resto() {
        return id_resto;
    }

    public void setId_resto(String id_resto) {
        this.id_resto = id_resto;
    }


    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_resto) {
        this.nama_menu = nama_resto;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getMenu_foto() {
        return menu_foto;
    }

    public void setMenu_foto(String menu_foto) {
        this.menu_foto = menu_foto;
    }

    public int getKetersediaan() {
        return ketersediaan;
    }

    public void setKetersediaan(int ketersediaan) {
        this.ketersediaan = ketersediaan;
    }
}
