package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.ItemModel;

public class ItemRespon {
    String kode, pesan;
    List<ItemModel> result;

    public List<ItemModel> getResult() {
        return result;
    }

    public void setResult(List<ItemModel> result) {
        this.result = result;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
