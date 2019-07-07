package com.example.androidtask.ui.githubmain;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.R;
import com.example.androidtask.adapters.ReposRecyclerViewAdapter;
import com.example.androidtask.model.GithubReposResponse;
import com.example.androidtask.ui.BaseActivity;
import com.example.androidtask.ui.custom.MessageDialog;
import com.example.androidtask.ui.custom.pagination.PaginationHelper;
import com.example.androidtask.ui.custom.pagination.PaginationInterface;
import com.example.androidtask.ui.repositorydetail.RepositoryDetailActivity;
import com.example.androidtask.ui.userdetail.UserDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GithubMainActivity extends BaseActivity implements PaginationInterface,
        ReposRecyclerViewAdapter.OnRepoItemClickListener,
        ReposRecyclerViewAdapter.OnRepoAvatarClickListener,
        GithubMainInterface.ViewInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvRepos)
    RecyclerView reposRecyclerView;

    @BindView(R.id.searchBar)
    CardView searchBar;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.ibSearch)
    ImageButton ibSearch;
    private boolean isLoading;
    private GithubReposResponse githubReposResponse;
    private ReposRecyclerViewAdapter adapter;
    private int perPage = 15;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private String searchedKeyWord;
    private GithubMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.GITHUB_REPOS);
        presenter = new GithubMainPresenter(this);
        adapter = new ReposRecyclerViewAdapter(this, this);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        reposRecyclerView.setLayoutManager(linearLayoutManager);
        reposRecyclerView.setAdapter(adapter);
        PaginationHelper paginationAbstract = new PaginationHelper(this);
        ibSearch.setEnabled(false);
        ibSearch.setAlpha(0.3f);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibSearch.setEnabled(!s.toString().isEmpty());
                ibSearch.setAlpha(s.toString().isEmpty() ? 0.3f : 1f);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick(R.id.ibSearch)
    public void onSearchImageButtonClicked() {
        adapter.clear();
        searchedKeyWord = etSearch.getText().toString().trim();
        isLoading = true;
        adapter.addLoadingFooter();
        presenter.getGithubRepos(searchedKeyWord, perPage, page);
    }


    @NonNull
    @Override
    public View passScrollView() {
        return reposRecyclerView;
    }

    @Nullable
    @Override
    public void loadMoreItems() {
        page = page + 1;
        presenter.getGithubRepos(searchedKeyWord, perPage, page);
    }

    @Nullable
    @Override
    public boolean isLastPage() {
        return (githubReposResponse != null) && (page + 1 >= (githubReposResponse.totalCount / perPage));
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

    @Override
    public void onRepoItemClicked(int itemId, String itemOwnerLogin) {
        Intent intent = new Intent(this, RepositoryDetailActivity.class);
        intent.putExtra(RepositoryDetailActivity.EXTRA_ITEM_ID, itemId);
        intent.putExtra(RepositoryDetailActivity.EXTRA_ITEM_OWNER_LOGIN, itemOwnerLogin);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View passOtherViewToAnimate() {
        return searchBar;
    }

    @Override
    public void onRepoAvatarClicked(String name) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void onDestroy() {
        //todo: call presenter.destroy();
        super.onDestroy();
    }

    @Override
    protected boolean actionBarBackButton() {
        return false;
    }

    @Override
    public void displayResult(GithubReposResponse githubReposResponse) {
        removeLoading();
        this.githubReposResponse = githubReposResponse;
        adapter.addAll(githubReposResponse.items);

    }

    @Override
    public void displayError(String errorMessage) {
        removeLoading();
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.setTitle(getString(R.string.ERROR));
        messageDialog.setContent(errorMessage != null ? errorMessage : getString(R.string.UNKOWN_ERROR));
        messageDialog.show();
    }

    private void removeLoading() {
        isLoading = false;
        adapter.removeLoadingFooter();
    }
}
