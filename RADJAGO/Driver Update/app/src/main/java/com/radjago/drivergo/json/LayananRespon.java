package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.LayananModel;

public class LayananRespon {
    String kode, pesan;
    List<LayananModel> result;

    public List<LayananModel> getResult() {
        return result;
    }

    public void setResult(List<LayananModel> result) {
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
