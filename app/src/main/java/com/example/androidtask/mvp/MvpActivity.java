package com.example.androidtask.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.androidtask.ui.BaseActivity;

public abstract class MvpActivity<P extends BasePresenterInterface> extends BaseActivity implements BaseViewInterface {

    /**
     * Initializes the presenter in the extending activity
     *
     * @return: returns the type of Presenter that is used in the extending activity
     */
    public abstract P initializePresenter();

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (presenter == null)
            presenter = initializePresenter();

        if (presenter != null)
            presenter.onAttach(this);

    }

    /**
     * Called when view is detached
     * Calls the presenter detach method to do clean up code.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null)
            presenter.onDetach();
    }
}
