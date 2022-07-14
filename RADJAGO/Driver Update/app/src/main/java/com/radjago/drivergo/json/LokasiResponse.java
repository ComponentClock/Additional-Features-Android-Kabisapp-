package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.LokasiModel;

public class LokasiResponse {
    String kode, pesan;
    List<LokasiModel> result;

    public List<LokasiModel> getResult() {
        return result;
    }

    public void setResult(List<LokasiModel> result) {
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

    }
}
