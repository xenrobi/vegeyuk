package com.example.vegeyuk.marketresto.models;

import java.io.Serializable;

public class Delivery implements Serializable {

    private String key;
    private String id;
    private String lat;
    private String lng;

    public Delivery(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return " "+id+"\n" +
                " "+lat +"\n" +
                " "+lng;
    }

    public Delivery(String x, String y, String z){
        id = x;
        lat = y;
        lng = z;

    }
}

