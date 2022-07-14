package com.radjago.drivergo.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.radjago.drivergo.models.AreaModels;

public class AreaResponse {

    @SerializedName("data")
    @Expose
    private List<AreaModels> data = new ArrayList<>();

    public List<AreaModels> getData() {
        return data;
    }

}
