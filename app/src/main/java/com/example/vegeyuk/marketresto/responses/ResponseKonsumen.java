package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseKonsumen {
    @SerializedName("data")
    @Expose
    private User data;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

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

}
