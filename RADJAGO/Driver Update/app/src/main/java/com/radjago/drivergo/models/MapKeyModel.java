package com.radjago.drivergo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public class MapKeyModel extends RealmObject implements Serializable {

    @PrimaryKey
    @Expose
    @SerializedName("com")
    private int id;

    @Expose
    @SerializedName("map_key")
    private String mapkey;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMapkey() {
        return mapkey;
    }

    public void setMapkey(String mapkey) {
        this.mapkey = mapkey;
    }
}
