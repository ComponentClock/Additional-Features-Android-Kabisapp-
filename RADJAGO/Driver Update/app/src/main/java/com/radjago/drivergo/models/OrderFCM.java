package com.radjago.drivergo.models;

import java.io.Serializable;

import com.radjago.drivergo.json.fcm.FCMType;

/**
 * Created by Ourdevelops Team on 19/10/2019.
 */
public class OrderFCM implements Serializable {
    public int type = FCMType.ORDER;
    public String id_driver;
    public String id_pelanggan;
    public String id_transaksi;
    public String response;
    public String desc;
    public String invoice;
    public String ordertime;
}
