package com.example.mypc.appchat.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MyPC on 12/03/2017.
 */

public class Member implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
