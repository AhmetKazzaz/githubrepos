package com.example.androidtask.ui.repositorydetail;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.example.androidtask.model.ui.UserCompleteData;
import com.example.androidtask.mvp.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * this presenter's job is to handle the logic side and networking
 * It fetches the data from the github API and exposes it to the view (Activity) to handle the UI updates
 */
public class RepositoryDetailPresenter extends BasePresenter<RepositoryInterface.ViewInterface>
        implements RepositoryInterface.PresenterInterface {

    /**
     * This methods calls 2 endpoints to get the repository and its owner.
     *
     * @param id:   the id of the github repository to fetch
     * @param name: the name of the owner of the github repository to fetch
     */
    @Override
    public void getRepoDataRequest(int id, String name) {
        Observable<GithubRepository> repositoryObservable =
                RetrofitClient.getInstance().endpoints().getGithubRepository(id);

        Observable<Owner> ownerObservable = RetrofitClient.getInstance().endpoints().
                getGithubUser(name);

        //the Zip function calls 2 separate parallel requests and merges the data into one source
        // so the subscription remains only one time
        Disposable disposable = Observable.zip(repositoryObservable, ownerObservable, (BiFunction<GithubRepository, Owner, Object>) (githubRepoitory, owner) -> {
            UserCompleteData userCompleteData = new UserCompleteData();
            userCompleteData.owner = owner;
            userCompleteData.githubRepository = githubRepoitory;


            return userCompleteData;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData, this::onError);
        addDisposable(disposable);
    }

    private void onData(Object data) {
        if (isViewAttached()) {
            getView().displayResult(((UserCompleteData) data));
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
