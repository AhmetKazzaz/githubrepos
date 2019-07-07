package com.example.androidtask.ui;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.androidtask.fragments.GithubReposListFragment;
import com.example.androidtask.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GithubMainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setFragment(new GithubReposListFragment());
        setSupportActionBar(toolbar);
        setTitle(R.string.GITHUB_REPOS);
    }

    @Override
    protected boolean actionBarBackButton() {
        return false;
    }

}
