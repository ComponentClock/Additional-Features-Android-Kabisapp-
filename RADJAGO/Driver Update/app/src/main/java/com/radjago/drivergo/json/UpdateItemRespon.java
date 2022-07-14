package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.UpdateItemModel;

public class UpdateItemRespon {
    String kode, pesan;
    List<UpdateItemModel> result;

    public List<UpdateItemModel> getResult() {
        return result;
    }

    public void setResult(List<UpdateItemModel> result) {
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
