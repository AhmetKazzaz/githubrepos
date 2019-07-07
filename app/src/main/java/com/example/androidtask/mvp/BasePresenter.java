package com.example.androidtask.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * This is a base class that handles attachment of view and getting rid of disposables for all extending classes
 *
 * @param <V>: Generic type of ViewInterface.
 */
public class BasePresenter<V extends BaseViewInterface> implements BasePresenterInterface<V> {

    private final CompositeDisposable disposableBag;
    private V view;

    public BasePresenter() {
        this.disposableBag = new CompositeDisposable();
    }

    public V getView() {
        return view;
    }

    /**
     * checks if the view is still attached
     *
     * @return: true if it is, false if not
     */
    public boolean isViewAttached() {
        return view != null;
    }

    /**
     * Attaches view to presenter
     *
     * @param view: the view to be attached
     */
    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    /**
     * gets rid of the disposable when the activity is destroyed
     */
    @Override
    public void onDetach() {
        disposableBag.dispose();
        view = null;
    }

    /**
     * adds the disposables given from extending classes to this composite disposable
     * so it can be disposed all together
     *
     * @param disposable: the given disposable from extending classes
     */
    protected void addDisposable(Disposable disposable) {
        disposableBag.add(disposable);
    }
}
