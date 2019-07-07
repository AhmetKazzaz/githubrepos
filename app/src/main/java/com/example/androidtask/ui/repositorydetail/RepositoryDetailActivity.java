package com.example.androidtask.ui.repositorydetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidtask.ui.BaseActivity;
import com.example.androidtask.ui.userdetail.UserDetailActivity;
import com.example.androidtask.model.ui.UserCompleteData;
import com.example.androidtask.R;
import com.example.androidtask.ui.custom.MessageDialog;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepositoryDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RepositoryInterface.viewInterface {

    public static final String EXTRA_ITEM_ID = "itemId";
    public static final String EXTRA_ITEM_OWNER_LOGIN = "itemOwnerLogin";

    @BindView(R.id.tvDefaultBranch)
    TextView tvDefaultBranch;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvForkCount)
    TextView tvForkCount;

    @BindView(R.id.tvLanguage)
    TextView tvLanguage;

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvRepoName)
    TextView tvRepoName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipeRefersh)
    SwipeRefreshLayout swipeRefersh;
    private RepositoryDetailPresenter presenter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.REPOSITORY_DETAIL);
        swipeRefersh.setOnRefreshListener(this);
        swipeRefersh.setRefreshing(true);
        presenter = new RepositoryDetailPresenter(this);
        presenter.getRepoDataRequest(
                getIntent().getIntExtra(EXTRA_ITEM_ID, -1),
                getIntent().getStringExtra(EXTRA_ITEM_OWNER_LOGIN)
        );
    }

    @OnClick(R.id.ivAvatar)
    public void onRepoAvatarClicked() {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_NAME, name);
        startActivity(intent);
    }

    @Override
    protected boolean actionBarBackButton() {
        return true;
    }

    @Override
    protected void onDestroy() {
        presenter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        swipeRefersh.setRefreshing(true);
        presenter.getRepoDataRequest(
                getIntent().getIntExtra(EXTRA_ITEM_ID, -1),
                getIntent().getStringExtra(EXTRA_ITEM_OWNER_LOGIN)
        );
    }

    @Override
    public void requestSuccess(UserCompleteData userCompleteData) {
        name = userCompleteData.owner.login;
        swipeRefersh.setRefreshing(false);
        tvDefaultBranch.setText(userCompleteData.githubRepository.defaultBranch);
        tvForkCount.setText(String.valueOf(userCompleteData.githubRepository.forks));
        if (userCompleteData.githubRepository.language != null
                && !userCompleteData.githubRepository.language.isEmpty())
            tvLanguage.setText(userCompleteData.githubRepository.language);

        tvRepoName.setText(userCompleteData.githubRepository.name);

        if (userCompleteData.owner.email != null &&
                !userCompleteData.owner.email.isEmpty())
            tvEmail.setText(userCompleteData.owner.email);

        tvUserName.setText(userCompleteData.owner.login);
        Picasso.get().load(userCompleteData.owner.avatar).into(ivAvatar);
    }

    @Override
    public void requestError(String message) {
        swipeRefersh.setRefreshing(false);
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.setTitle(getString(R.string.ERROR));
        messageDialog.setContent(message != null ? message : getString(R.string.UNKOWN_ERROR));
        messageDialog.show();
    }
}
