package com.radjago.drivergo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public class FcmKeyModel extends RealmObject implements Serializable {

    @PrimaryKey
    @Expose
    @SerializedName("com")
    private int id;

    @Expose
    @SerializedName("fcm_key")
    private String fcmkey;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFcmkey() {
        return fcmkey;
    }

    public void setFcmkey(String fcmkey) {
        this.fcmkey = fcmkey;
    }
}
