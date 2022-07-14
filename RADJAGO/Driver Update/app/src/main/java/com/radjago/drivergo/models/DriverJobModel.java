package com.radjago.drivergo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class DriverJobModel extends RealmObject implements Serializable {

    @PrimaryKey
    @Expose
    @SerializedName("com")
    private String id;

    @Expose
    @SerializedName("id_driver")
    private String idDriver;

    @Expose
    @SerializedName("job")
    private String job;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

}
