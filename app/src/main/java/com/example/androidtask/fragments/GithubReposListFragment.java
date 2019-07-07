package com.example.androidtask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.R;
import com.example.androidtask.adapters.ReposRecyclerViewAdapter;
import com.example.androidtask.api.ErrorResponse;
import com.example.androidtask.api.RetrofitClient;
import com.example.androidtask.api.RetrofitException;
import com.example.androidtask.model.GithubReposResponse;
import com.example.androidtask.ui.custom.pagination.PaginationHelper;
import com.example.androidtask.ui.custom.pagination.PaginationInterface;
import com.example.androidtask.ui.repositorydetail.RepositoryDetailActivity;
import com.example.androidtask.ui.userdetail.UserDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GithubReposListFragment extends Fragment implements PaginationInterface, ReposRecyclerViewAdapter.OnRepoItemClickListener, ReposRecyclerViewAdapter.OnRepoAvatarClickListener {

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
    private Disposable reposDisposable;
    private ReposRecyclerViewAdapter adapter;
    private int perPage = 15;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private String searchedKeyWord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.github_repos_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        adapter = new ReposRecyclerViewAdapter(this, this);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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
        getGithubRepos(searchedKeyWord, perPage, page);
    }

    private void getGithubRepos(@Nullable String qualifier, int perPage, int page) {
        isLoading = true;
        adapter.addLoadingFooter();
        reposDisposable = RetrofitClient.getInstance().endpoints().gitGithubRepos(qualifier, perPage, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        githubReposResponse -> {
                            isLoading = false;
                            adapter.removeLoadingFooter();
                            this.githubReposResponse = githubReposResponse;
                            adapter.addAll(githubReposResponse.items);

                        }, error -> {
                            isLoading = false;
                            adapter.removeLoadingFooter();
                            ErrorResponse response = ((RetrofitException) error).errorResponse();
                            if (response != null) {
//                                messageDialog.setMessage(response.message);
                            } else {
//                                messageDialog.setMessage(getString(R.string.UNKOWN_ERROR));
                            }
//                            messageDialog.show();
                        }

                );
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
        getGithubRepos(searchedKeyWord, perPage, page);
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
        Intent intent = new Intent(getContext(), RepositoryDetailActivity.class);
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
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (reposDisposable != null && !reposDisposable.isDisposed())
            reposDisposable.dispose();

        super.onDestroy();
    }
}
