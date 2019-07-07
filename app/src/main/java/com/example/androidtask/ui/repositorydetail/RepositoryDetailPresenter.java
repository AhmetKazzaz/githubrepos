package com.example.androidtask.ui.repositorydetail;

import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.example.androidtask.model.ui.UserCompleteData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * this presenter's job is to handle the logic side and networking
 * It fetches the data from the github API and exposes it to the view (Activity) to handle the UI updates
 */
public class RepositoryDetailPresenter implements RepositoryInterface.presenterInterface {

    private RepositoryInterface.viewInterface viewInterface;
    private Disposable disposable;

    RepositoryDetailPresenter(RepositoryInterface.viewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }


    /**
     * This methods calls 2 endpoints to get the repository and its owner.
     *
     * @param id:   the id of the github repository to fetch
     * @param name: the name of the owner of the github repository to fetch
     */
    @Override
    public void getRepoDataRequest(int id, String name) {
        Observable<GithubRepository> repositoryObservable =
                RetrofitClient.getInstance().endpoints().gitGithubRepository(id);

        Observable<Owner> ownerObservable = RetrofitClient.getInstance().endpoints().
                getGithubUser(name);

        //the Zip function calls 2 separate parallel requests and merges the data into one source
        // so the subscription remains only one time
        disposable = Observable.zip(repositoryObservable, ownerObservable, (BiFunction<GithubRepository, Owner, Object>) (githubRepoitory, owner) -> {
            UserCompleteData userCompleteData = new UserCompleteData();
            userCompleteData.owner = owner;
            userCompleteData.githubRepository = githubRepoitory;

            return userCompleteData;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> viewInterface.requestSuccess(((UserCompleteData) data)),
                        error -> {
                            ErrorResponse response = ((RetrofitException) error).errorResponse();
                            if (response != null) {
                                viewInterface.requestError(response.message);
                            } else {
                                viewInterface.requestError(null);
                            }
                        }
                );
    }

    /**
     * gets rid of the disposable when the activity is destroyed
     */
    @Override
    public void onActivityDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
