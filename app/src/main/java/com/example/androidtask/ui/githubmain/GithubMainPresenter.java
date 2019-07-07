package com.example.androidtask.ui.githubmain;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GithubMainPresenter implements GithubMainInterface.PresenterInterface {

    private GithubMainInterface.ViewInterface viewInterface;
    private Disposable reposDisposable;

    GithubMainPresenter(GithubMainInterface.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void getGithubRepos(String qualifier, int perPage, int page) {
        reposDisposable = RetrofitClient.getInstance().endpoints().gitGithubRepos(qualifier, perPage, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        githubReposResponse -> {
                            viewInterface.displayResult(githubReposResponse);
                        }, error -> {
                            ErrorResponse response = ((RetrofitException) error).errorResponse();
                            if (response != null) {
                                viewInterface.displayError(response.message);
                            } else {
                                viewInterface.displayError(null);
                            }
                        }

                );
    }
}
