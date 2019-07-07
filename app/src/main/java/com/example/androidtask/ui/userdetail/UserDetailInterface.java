package com.example.androidtask.ui.userdetail;

import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

public interface UserDetailInterface {

    interface ViewInterface {
        void displayResult(Owner owner, Result<ArrayList<GithubRepository>> result);
        void displayError(String error);
    }

    interface PresenterInterface {
        void getUserDataRequest(String name, int perPage, int page);
    }


}
