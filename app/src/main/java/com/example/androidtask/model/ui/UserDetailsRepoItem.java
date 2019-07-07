package com.example.androidtask.model.ui;

import com.example.androidtask.model.GithubRepository;

import java.util.ArrayList;

public class UserDetailsRepoItem extends UserInfoBaseItem {

    public ArrayList<GithubRepository> githubRepoitories;

    public UserDetailsRepoItem(ArrayList<GithubRepository> githubRepoitories) {
        this.githubRepoitories = githubRepoitories;
    }

    @Override
    public UserInfoItemType type() {
        return UserInfoItemType.repos;
    }
}
