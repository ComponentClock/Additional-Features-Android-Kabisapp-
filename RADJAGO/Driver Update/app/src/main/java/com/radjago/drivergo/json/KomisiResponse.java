package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.FiturModel;

public class KomisiResponse {
    String kode, pesan;
    List<FiturModel> result;

    public List<FiturModel> getResult() {
        return result;
    }

    public void setResult(List<FiturModel> result) {
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
