package com.example.mypc.appchat.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 12/03/2017.
 */

public class Contact implements Serializable{
    @SerializedName("message")
    private List<Message> message = new ArrayList<Message>();

    @SerializedName("_id")
    private String id;

    @SerializedName("members")
    private List<Member> members = new ArrayList<Member>();

    @SerializedName("color")
    private Boolean color;

    public Boolean getColor() {
        return color;
    }

    public void setColor(Boolean color) {
        this.color = color;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
