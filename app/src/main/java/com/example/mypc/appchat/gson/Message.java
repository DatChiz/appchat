package com.example.mypc.appchat.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MyPC on 11/03/2017.
 */

public class Message implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("body")
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
