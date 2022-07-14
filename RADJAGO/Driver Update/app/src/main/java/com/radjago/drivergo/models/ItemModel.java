package com.radjago.drivergo.models;

public class ItemModel {
    String nama_item, harga_item, harga_promo, foto_item, status_promo;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_item() {
        return nama_item;
    }

    public void setNama_item(String nama_item) {
        this.nama_item = nama_item;
    }

    public String getHarga_item() {
        return harga_item;
    }

    public void setHarga_item(String harga_item) {
        this.harga_item = harga_item;
    }

    public String getHarga_promo() {
        return harga_promo;
    }

    public void setHarga_promo(String harga_promo) {
        this.harga_promo = harga_promo;
    }

    public String getFoto_item() {
        return foto_item;
    }

    public void setFoto_item(String foto_item) {
        this.foto_item = foto_item;
    }

    public String getStatus_promo() {
        return status_promo;
    }

    public void setStatus_promo(String status_promo) {
        this.status_promo = status_promo;
    }
}
