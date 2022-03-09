package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.Favorit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFavorit {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("favorit")
    @Expose
    private List<Favorit> favorit = null;

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

    public List<Favorit> getFavorit() {
        return favorit;
    }

    public void setFavorit(List<Favorit> favorit) {
        this.favorit = favorit;
    }

}
