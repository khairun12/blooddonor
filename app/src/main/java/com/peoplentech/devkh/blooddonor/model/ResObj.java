package com.peoplentech.devkh.blooddonor.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

public class ResObj {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("user")
    private User user;

    private ArrayList<User> users;

    public ResObj () {

    }

    public ResObj (Boolean error, User user) {
        this.error = error;
        this.user = user;
    }

    public Boolean getError() {
        return error;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
