package com.example.androidtask.mvp;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter {

    CompositeDisposable disposable;

    public void onActivityDestroy(){

    }
}
