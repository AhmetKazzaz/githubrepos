package com.example.androidtask.ui.githubmain;

import com.example.androidtask.model.GithubReposResponse;

public interface GithubMainInterface {

    interface ViewInterface {
        void displayResult(GithubReposResponse githubReposResponse);

        void displayError(String errorMessage);
    }

    interface PresenterInterface {
        void getGithubRepos(String qualifier, int perPage, int page);
    }

}
