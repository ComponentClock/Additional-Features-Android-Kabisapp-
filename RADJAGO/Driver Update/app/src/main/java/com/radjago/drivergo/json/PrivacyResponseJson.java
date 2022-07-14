package com.radjago.drivergo.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.radjago.drivergo.models.SettingsModel;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class PrivacyResponseJson {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<SettingsModel> data = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SettingsModel> getData() {
        return data;
    }

    public void setData(List<SettingsModel> data) {
        this.data = data;
    }
}
