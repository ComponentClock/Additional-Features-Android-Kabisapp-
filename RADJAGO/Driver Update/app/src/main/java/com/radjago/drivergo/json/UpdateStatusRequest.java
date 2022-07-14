package com.radjago.drivergo.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateStatusRequest {
    @SerializedName("com")
    @Expose
    private String id;

    @SerializedName("id_driver")
    @Expose
    private String id_driver;

    @SerializedName("id_transaksi")
    @Expose
    private String id_transaksi;

    @SerializedName("total_biaya")
    @Expose
    private String total_biaya;

    @SerializedName("fitur")
    @Expose
    private String fitur;

    @SerializedName("nama_driver")
    @Expose
    private String nama_driver;

    @SerializedName("foto_driver")
    @Expose
    private String foto_driver;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("israte")
    @Expose
    private int israte;

    @SerializedName("pakai_wallet")
    @Expose
    private String pakai_wallet;

    @SerializedName("point_driver")
    @Expose
    private String point_driver;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_driver() {
        return id;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }


    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }

    public String getFitur() {
        return fitur;
    }

    public void setFitur(String fitur) {
        this.fitur = fitur;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getFoto_driver() {
        return foto_driver;
    }

    public void setFoto_driver(String foto_driver) {
        this.foto_driver = foto_driver;
    }

    public int getIsrate() {
        return israte;
    }

    public void setIsrate(int israte) {
        this.israte = israte;
    }

    public String getPakai_wallet() {
        return pakai_wallet;
    }

    public void setPakai_wallet(String pakai_wallet) {
        this.pakai_wallet = pakai_wallet;
    }

    public String getPoint_driver() {
        return point_driver;
    }

    public void setPoint_driver(String point_driver) {
        this.point_driver = point_driver;
    }


}