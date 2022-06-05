package com.example.vegeyuk.marketresto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("konsumen_nama")
    @Expose
    private String konsumenNama;
    @SerializedName("konsumen_email")
    @Expose
    private String konsumenEmail;
    @SerializedName("konsumen_phone")
    @Expose
    private String konsumenPhone;
    @SerializedName("konsumen_balance")
    @Expose
    private String konsumenBalance;
    @SerializedName("konsumen_blacklist")
    @Expose
    private Integer konsumenBlacklist;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("kode")
    @Expose
    private String kode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKonsumenNama() {
        return konsumenNama;
    }

    public void setKonsumenNama(String konsumenNama) {
        this.konsumenNama = konsumenNama;
    }

    public String getKonsumenEmail() {
        return konsumenEmail;
    }

    public void setKonsumenEmail(String konsumenEmail) {
        this.konsumenEmail = konsumenEmail;
    }

    public String getKonsumenPhone() {
        return konsumenPhone;
    }

    public void setKonsumenPhone(String konsumenPhone) {
        this.konsumenPhone = konsumenPhone;
    }

    public String getKonsumenBalance() {
        return konsumenBalance;
    }

    public void setKonsumenBalance(String konsumenBalance) {
        this.konsumenBalance = konsumenBalance;
    }

    public Integer getKonsumenBlacklist() {
        return konsumenBlacklist;
    }

    public void setKonsumenBlacklist(Integer konsumenBlacklist) {
        this.konsumenBlacklist = konsumenBlacklist;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

}
