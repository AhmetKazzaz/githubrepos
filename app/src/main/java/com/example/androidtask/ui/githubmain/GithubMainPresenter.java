package com.example.androidtask.ui.githubmain;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubReposResponse;
import com.example.androidtask.mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * this presenter's job is to handle the logic side and networking
 * It fetches the data from the github API and exposes it to the view (Activity) to handle the UI updates
 */
public class GithubMainPresenter extends BasePresenter<GithubMainInterface.ViewInterface>
        implements GithubMainInterface.PresenterInterface {


    /**
     * This method calls the endpoint which gets public repos based on a searched keyword
     *
     * @param qualifier: the keyword to search for
     * @param perPage:   The number of items to be fetched in one page
     * @param page:      The exact page number to be fetched
     */
    @Override
    public void getGithubRepos(String qualifier, int perPage, int page) {
        Disposable reposDisposable = RetrofitClient.getInstance().endpoints().getGithubRepos(qualifier, perPage, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData, this::onError);
        addDisposable(reposDisposable);
    }

    private void onData(Object data) {
        if (isViewAttached()) {
            getView().displayResult(((GithubReposResponse) data));
        }
    }

    private void onError(Throwable error) {
        if (isViewAttached()) {
            ErrorResponse response = ((RetrofitException) error).errorResponse();
            if (response != null) {
                getView().displayError(response.message);
            } else {
                getView().displayError(null);
            }
        }
    }
}
