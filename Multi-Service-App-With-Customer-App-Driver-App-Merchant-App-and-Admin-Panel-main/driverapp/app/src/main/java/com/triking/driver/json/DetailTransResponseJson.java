package com.triking.driver.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.triking.driver.models.ItemOrderModel;
import com.triking.driver.models.CustomerModel;
import com.triking.driver.models.TransModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class DetailTransResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<TransModel> data = new ArrayList<>();

    @Expose
    @SerializedName("customer")
    private List<CustomerModel> pelanggan = new ArrayList<>();

    @Expose
    @SerializedName("item")
    private List<ItemOrderModel> item = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransModel> getData() {
        return data;
    }

    public void setData(List<TransModel> data) {
        this.data = data;
    }

    public List<CustomerModel> getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(List<CustomerModel> pelanggan) {
        this.pelanggan = pelanggan;
    }

    public List<ItemOrderModel> getItem() {
        return item;
    }

    public void setItem(List<ItemOrderModel> item) {
        this.item = item;
    }
}
