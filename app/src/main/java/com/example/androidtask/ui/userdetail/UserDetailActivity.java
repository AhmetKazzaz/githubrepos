package com.example.androidtask.ui.userdetail;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.ui.BaseActivity;
import com.example.androidtask.adapters.UserDetailListAdapter;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.Owner;
import com.example.androidtask.model.ui.UserCompleteData;
import com.example.androidtask.ui.custom.pagination.PaginationHelper;
import com.example.androidtask.ui.custom.pagination.PaginationInterface;
import com.example.androidtask.R;
import com.example.androidtask.ui.custom.MessageDialog;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailActivity extends BaseActivity implements PaginationInterface,
        UserDetailInterface.ViewInterface {

    public static final String EXTRA_NAME = "name";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llError)
    LinearLayout llError;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private int perPage = 10;
    private int page = 1;
    private PaginationHelper paginationAbstract;
    private String lastPage;
    private UserDetailListAdapter adapter;
    private UserDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.USER_DETAILS);
        presenter = new UserDetailPresenter(this);
        paginationAbstract = new PaginationHelper(this);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new UserDetailListAdapter();
        recyclerView.setAdapter(adapter);
        isLoading = true;

        adapter.addLoadingFooter();

        presenter.getUserDataRequest(getIntent().getStringExtra(EXTRA_NAME), perPage, page);
    }


    @Override
    protected boolean actionBarBackButton() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View passOtherViewToAnimate() {
        return null;
    }

    @Nullable
    @Override
    public void loadMoreItems() {
        isLoading = true;
        adapter.addLoadingFooter();
        page = page + 1;
        presenter.getUserDataRequest(getIntent().getStringExtra(EXTRA_NAME), perPage, page);
    }

    @Nullable
    @Override
    public boolean isLastPage() {
        return lastPage == null;
    }

    @Nullable
    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Nullable
    @Override
    public LinearLayoutManager passLinearLayoutManager() {
        return linearLayoutManager;
    }

    @NonNull
    @Override
    public View passScrollView() {
        return recyclerView;
    }

    @Override
    public void displayResult(Owner owner, Result<ArrayList<GithubRepository>> result) {
        setLoadingFalse();
        ArrayList<UserCompleteData> dataList = new ArrayList<>();
        if (page == 1) {
            UserCompleteData data = new UserCompleteData();
            data.owner = owner;
            data.githubRepository = null;
            data.type = 1;
            dataList.add(data);
        }
        lastPage = paginationAbstract.linkDecode(result.response().headers().get("link"));
        for (GithubRepository githubRepository : result.response().body()) {
            UserCompleteData completeData = new UserCompleteData();
            completeData.type = 2;
            completeData.githubRepository = githubRepository;
            completeData.owner = null;
            dataList.add(completeData);
        }
        adapter.addAll(dataList);
    }

    @OnClick(R.id.ibRefresh)
    public void onRefreshPageClicked() {
        setLoadingFalse();
        presenter.getUserDataRequest(getIntent().getStringExtra(EXTRA_NAME), perPage, page);
    }

    private void setLoadingFalse() {
        isLoading = false;
        adapter.removeLoadingFooter();
    }

    @Override
    public void displayError(String message) {
        llError.setVisibility(View.VISIBLE);
        setLoadingFalse();
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.setTitle(getString(R.string.ERROR));
        messageDialog.setContent(message != null ? message : getString(R.string.UNKOWN_ERROR));
        messageDialog.show();
    }
}
