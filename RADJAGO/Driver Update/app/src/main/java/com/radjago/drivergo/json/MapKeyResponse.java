package com.radjago.drivergo.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.radjago.drivergo.models.MapKeyModel;

public class MapKeyResponse {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<MapKeyModel> data = new ArrayList<>();


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MapKeyModel> getData() {
        return data;
    }

    public void setData(List<MapKeyModel> data) {
        this.data = data;
    }
}
