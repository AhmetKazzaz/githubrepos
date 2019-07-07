package com.example.androidtask.ui.repositorydetail;

import com.example.androidtask.model.ui.UserCompleteData;

public interface RepositoryInterface {

    interface viewInterface {
        void requestSuccess(UserCompleteData userCompleteData);

        void requestError(String error);
    }

    interface presenterInterface {
        void getRepoDataRequest(int id, String name);
        void onActivityDestroy();
    }
}
