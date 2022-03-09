package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseOneRestoran {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("konsumen_balance")
    @Expose
    private String konsumenBalance;
    @SerializedName("data")
    @Expose
    private Restoran data;
    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;

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

    public Restoran getData() {
        return data;
    }

    public void setData(Restoran data) {
        this.data = data;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public String getKonsumenBalance() {
        return konsumenBalance;
    }

    public void setKonsumenBalance(String konsumenBalance) {
        this.konsumenBalance = konsumenBalance;
    }
}
