package com.example.vegeyuk.marketresto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_konsumen")
    @Expose
    private Integer idKonsumen;
    @SerializedName("id_restoran")
    @Expose
    private Integer idRestoran;
    @SerializedName("order_lat")
    @Expose
    private String orderLat;
    @SerializedName("order_long")
    @Expose
    private String orderLong;
    @SerializedName("order_alamat")
    @Expose
    private String orderAlamat;
    @SerializedName("order_catatan")
    @Expose
    private String orderCatatan;
    @SerializedName("order_metode_bayar")
    @Expose
    private String orderMetodeBayar;
    @SerializedName("order_jarak_antar")
    @Expose
    private String orderJarakAntar;
    @SerializedName("order_biaya_anatar")
    @Expose
    private String orderBiayaAnatar;
    @SerializedName("order_pajak_pb_satu")
    @Expose
    private Integer order_pajak_pb_satu;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("order_restoran")
    @Expose
    private String orderRestoran;
    @SerializedName("order_konsumen")
    @Expose
    private String orderKonsumen;
    @SerializedName("order_konsumen_phone")
    @Expose
    private String orderKonsumenPhone;
    @SerializedName("detail_order")
    @Expose
    private List<Menu> detailOrder = null;

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

    public Integer getIdRestoran() {
        return idRestoran;
    }

    public void setIdRestoran(Integer idRestoran) {
        this.idRestoran = idRestoran;
    }

    public String getOrderLat() {
        return orderLat;
    }

    public void setOrderLat(String orderLat) {
        this.orderLat = orderLat;
    }

    public String getOrderLong() {
        return orderLong;
    }

    public void setOrderLong(String orderLong) {
        this.orderLong = orderLong;
    }

    public String getOrderAlamat() {
        return orderAlamat;
    }

    public void setOrderAlamat(String orderAlamat) {
        this.orderAlamat = orderAlamat;
    }

    public String getOrderCatatan() {
        return orderCatatan;
    }

    public void setOrderCatatan(String orderCatatan) {
        this.orderCatatan = orderCatatan;
    }

    public String getOrderMetodeBayar() {
        return orderMetodeBayar;
    }

    public void setOrderMetodeBayar(String orderMetodeBayar) {
        this.orderMetodeBayar = orderMetodeBayar;
    }

    public String getOrderJarakAntar() {
        return orderJarakAntar;
    }

    public void setOrderJarakAntar(String orderJarakAntar) {
        this.orderJarakAntar = orderJarakAntar;
    }

    public String getOrderBiayaAnatar() {
        return orderBiayaAnatar;
    }

    public void setOrderBiayaAnatar(String orderBiayaAnatar) {
        this.orderBiayaAnatar = orderBiayaAnatar;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderRestoran() {
        return orderRestoran;
    }

    public void setOrderRestoran(String orderRestoran) {
        this.orderRestoran = orderRestoran;
    }

    public String getOrderKonsumen() {
        return orderKonsumen;
    }

    public void setOrderKonsumen(String orderKonsumen) {
        this.orderKonsumen = orderKonsumen;
    }

    public String getOrderKonsumenPhone() {
        return orderKonsumenPhone;
    }

    public void setOrderKonsumenPhone(String orderKonsumenPhone) {
        this.orderKonsumenPhone = orderKonsumenPhone;
    }

    public List<Menu> getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(List<Menu> detailOrder) {
        this.detailOrder = detailOrder;
    }

    public Integer getOrder_pajak_pb_satu() {
        return order_pajak_pb_satu;
    }

    public void setOrder_pajak_pb_satu(Integer order_pajak_pb_satu) {
        this.order_pajak_pb_satu = order_pajak_pb_satu;
    }
}