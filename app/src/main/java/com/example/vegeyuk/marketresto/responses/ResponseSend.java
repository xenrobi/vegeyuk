package com.example.vegeyuk.marketresto.responses;

import com.example.vegeyuk.marketresto.models.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSend {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private Message message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
