package com.example.androidtask.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GithubReposResponse {

    @SerializedName("total_count")
    public int totalCount;

    public ArrayList<GithubRepository> items;
}
