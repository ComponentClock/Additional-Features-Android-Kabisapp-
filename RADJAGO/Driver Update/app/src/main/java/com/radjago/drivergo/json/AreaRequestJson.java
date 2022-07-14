package com.radjago.drivergo.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaRequestJson {

    @SerializedName("kota")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
