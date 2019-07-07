package com.example.androidtask.ui.userdetail;

import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.example.androidtask.mvp.BasePresenterInterface;
import com.example.androidtask.mvp.BaseViewInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

public interface UserDetailInterface {

    interface ViewInterface extends BaseViewInterface {
        void displayResult(Owner owner, Result<ArrayList<GithubRepository>> result);

        void displayError(String error);
    }

    interface PresenterInterface extends BasePresenterInterface<ViewInterface> {
        void getUserDataRequest(String name, int perPage, int page);
    }


}
