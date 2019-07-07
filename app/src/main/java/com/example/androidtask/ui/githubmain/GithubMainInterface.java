package com.example.androidtask.ui.githubmain;

import com.example.androidtask.model.GithubReposResponse;
import com.example.androidtask.mvp.BasePresenterInterface;
import com.example.androidtask.mvp.BaseViewInterface;

public interface GithubMainInterface {

    interface ViewInterface extends BaseViewInterface {
        void displayResult(GithubReposResponse githubReposResponse);

        void displayError(String errorMessage);
    }

    interface PresenterInterface extends BasePresenterInterface<ViewInterface> {
        void getGithubRepos(String qualifier, int perPage, int page);
    }

}
