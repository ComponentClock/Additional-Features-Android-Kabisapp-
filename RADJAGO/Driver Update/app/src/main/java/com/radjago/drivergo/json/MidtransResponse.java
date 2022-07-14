package com.radjago.drivergo.json;
import java.util.List;

import com.radjago.drivergo.models.MidtransModels;

public class MidtransResponse {
    String  kode, pesan;
    List<MidtransModels> result;

    public List<MidtransModels> getResult() {
        return result;
    }

    public void setResult(List<MidtransModels> result) {
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
