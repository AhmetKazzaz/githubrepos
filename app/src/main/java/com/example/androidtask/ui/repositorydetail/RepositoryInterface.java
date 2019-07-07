package com.example.androidtask.ui.repositorydetail;

import com.example.androidtask.model.ui.UserCompleteData;
import com.example.androidtask.mvp.BasePresenterInterface;
import com.example.androidtask.mvp.BaseViewInterface;

public interface RepositoryInterface {

    interface ViewInterface extends BaseViewInterface {
        void displayResult(UserCompleteData userCompleteData);

        void displayError(String error);
    }

    interface PresenterInterface extends BasePresenterInterface<ViewInterface> {
        void getRepoDataRequest(int id, String name);
    }
}
