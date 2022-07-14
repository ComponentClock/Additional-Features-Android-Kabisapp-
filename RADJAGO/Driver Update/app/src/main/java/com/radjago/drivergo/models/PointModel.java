package com.radjago.drivergo.models;

public class PointModel {
    String nama_promo, point, type_promo, nominal_promo, expired, image_promo, status;
    int id_promo;

    public int getId_promo() {
        return id_promo;
    }

    public void setId_promo(int id_promo) {
        this.id_promo = id_promo;
    }

    public String getNama_promo() {
        return nama_promo;
    }

    public void setNama_promo(String nama_promo) {
        this.nama_promo = nama_promo;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getType_promo() {
        return type_promo;
    }

    public void setType_promo(String type_promo) {
        this.type_promo = type_promo;
    }

    public String getNominal_promo() {
        return nominal_promo;
    }

    public void setNominal_promo(String nominal_promo) {
        this.nominal_promo = nominal_promo;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getImage_promo() {
        return image_promo;
    }

    public void setImage_promo(String image_promo) {
        this.image_promo = image_promo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
