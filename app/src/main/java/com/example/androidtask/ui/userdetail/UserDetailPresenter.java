package com.example.androidtask.ui.userdetail;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.example.androidtask.mvp.BasePresenter;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * this presenter's job is to handle the logic side and networking
 * It fetches the data from the github API and exposes it to the view (Activity) to handle the UI updates
 */
public class UserDetailPresenter extends BasePresenter<UserDetailInterface.ViewInterface>
        implements UserDetailInterface.PresenterInterface {

    private Owner owner;
    private CompositeDisposable compositeDisposable;

    UserDetailPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * Gets the user data (chained requests)
     *
     * @param name:    User name (login)
     * @param perPage: The count of the items to be fetched per page
     * @param page:    The exact page to be fetched
     */
    @Override
    public void getUserDataRequest(String name, int perPage, int page) {
        compositeDisposable.add(call(name, RetrofitClient.getInstance().endpoints().
                getGithubUserRepos(name, perPage, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onData, this::onError));

        addDisposable(compositeDisposable);
    }

    private Observable<Result<ArrayList<GithubRepository>>> call(
            String name,
            Observable<Result<ArrayList<GithubRepository>>> apiCall) {
        return get(name)
                .flatMap((Function<String, ObservableSource<Result<ArrayList<GithubRepository>>>>) s -> apiCall);
    }


    /**
     * Requesting the user details first then returning only the required field to the emitter.
     *
     * @param name: the user name(login); required field for getting the user repos
     * @return: returns a String observable to be mapped to another request.
     */
    private Observable<String> get(String name) {
        return Observable.create(emitter -> compositeDisposable.add(RetrofitClient.getInstance().endpoints()
                .getGithubUser(name)
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .flatMap(owner -> {
                    this.owner = owner;
                    return Observable.just(owner.login);
                })
                .subscribe(emitter::onNext, emitter::onError)));
    }

    private void onData(Object data) {
        if (isViewAttached()) {
            getView().displayResult(owner, ((Result<ArrayList<GithubRepository>>) data));
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
