package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSearch {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("restoran")
    @Expose
    private List<Restoran> restoran = null;
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

    public List<Restoran> getRestoran() {
        return restoran;
    }

    public void setRestoran(List<Restoran> restoran) {
        this.restoran = restoran;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }
}
