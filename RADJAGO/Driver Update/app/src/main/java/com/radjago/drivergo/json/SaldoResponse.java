package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.SaldoModel;

public class SaldoResponse {
    String kode, pesan;
    List<SaldoModel> result;

    public List<SaldoModel> getResult() {
        return result;
    }

    public void setResult(List<SaldoModel> result) {
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
