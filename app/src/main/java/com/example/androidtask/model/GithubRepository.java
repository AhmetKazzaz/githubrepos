package com.example.androidtask.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GithubRepository implements Serializable {
    public int id;
    public String description;
    public String name;
    public int forks;
    public String language;
    @SerializedName("default_branch")
    public String defaultBranch;
    public Owner owner;
}
