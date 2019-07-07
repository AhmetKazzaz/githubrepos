package com.example.androidtask.ui.userdetail;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserDetailPresenter implements UserDetailInterface.PresenterInterface {

    private UserDetailInterface.ViewInterface viewInterface;
    private Owner owner;
    private CompositeDisposable compositeDisposable;

    UserDetailPresenter(UserDetailInterface.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getUserDataRequest(String name, int perPage, int page) {
        compositeDisposable.add(call(name, RetrofitClient.getInstance().endpoints().
                getGithubUserRepos(name, perPage, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> viewInterface.displayResult(owner, result),
                        error -> {
                            ErrorResponse response = ((RetrofitException) error).errorResponse();
                            if (response != null) {
                                viewInterface.displayError(response.message);
                            } else {
                                viewInterface.displayError(null);
                            }
                        }));
    }

    private Observable<Result<ArrayList<GithubRepository>>> call(
            String name,
            Observable<Result<ArrayList<GithubRepository>>> apiCall) {
        return get(name)
                .flatMap((Function<String, ObservableSource<Result<ArrayList<GithubRepository>>>>) s -> apiCall);
    }


    Observable<String> get(String name) {
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
}
