package com.example.androidtask.model;

import com.google.gson.annotations.SerializedName;

public class Owner {
    @SerializedName("avatar_url")
    public String avatar;
    public String name;
    public String login;
    public String email;

}
