package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.WalletModels;

public class WalletRespon {
    String  kode, pesan;
    List<WalletModels> result;

    public List<WalletModels> getResult() {
        return result;
    }

    public void setResult(List<WalletModels> result) {
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

