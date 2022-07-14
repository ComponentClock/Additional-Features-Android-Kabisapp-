package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.UpdateTransaksiModel;

public class TransaksiRespon {
    String kode, pesan;
    List<UpdateTransaksiModel> result;

    public List<UpdateTransaksiModel> getResult() {
        return result;
    }

    public void setResult(List<UpdateTransaksiModel> result) {
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
