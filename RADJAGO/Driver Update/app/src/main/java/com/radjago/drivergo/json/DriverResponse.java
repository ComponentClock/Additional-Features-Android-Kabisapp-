package com.radjago.drivergo.json;

import java.util.List;

import com.radjago.drivergo.models.DriverModel;

public class DriverResponse {
    String kode, pesan;
    List<DriverModel> result;

    public List<DriverModel> getResult() {
        return result;
    }

    public void setResult(List<DriverModel> result) {
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
