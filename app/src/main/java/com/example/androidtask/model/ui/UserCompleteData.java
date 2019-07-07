package com.example.androidtask.model.ui;

import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;

/**
 * A class that holds both the repository and its owner data in one place
 */
public class UserCompleteData {
    public int type; //the type of data (eg: ownerDetails or repo)
    public Owner owner;
    public GithubRepository githubRepository;

    public UserCompleteData() {
    }
}
