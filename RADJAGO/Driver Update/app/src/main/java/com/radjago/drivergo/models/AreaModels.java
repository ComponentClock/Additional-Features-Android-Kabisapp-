package com.radjago.drivergo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaModels {
    @SerializedName("com")
    @Expose
    private String id;

    @SerializedName("kota")
    @Expose
    private String kota;

    @SerializedName("promo")
    @Expose
    private String promo;

    @SerializedName("rate1")
    @Expose
    private String rate1;

    @SerializedName("rate2")
    @Expose
    private String rate2;

    @SerializedName("rate3")
    @Expose
    private String rate3;

    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public String getKota() {
        return kota;
    }

    public String getPromo() {
        return promo;
    }

    public String getRate1() {
        return rate1;
    }

    public String getRate2() {
        return rate2;
    }

    public String getRate3() {
        return rate3;
    }

    public String getStatus() {
        return status;
    }
}
