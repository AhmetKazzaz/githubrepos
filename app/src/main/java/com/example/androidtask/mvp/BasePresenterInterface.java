package com.example.androidtask.mvp;

public interface BasePresenterInterface<V extends BaseViewInterface> {

    void onAttach(V view);

    void onDetach();

}
