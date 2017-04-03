package com.example.mypc.appchat.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 11/03/2017.
 */

public class Conversation implements Serializable{
    @SerializedName("_id")
    private String id;

    @SerializedName("contact")
    private List<Contact> contact = new ArrayList<Contact>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }
}
