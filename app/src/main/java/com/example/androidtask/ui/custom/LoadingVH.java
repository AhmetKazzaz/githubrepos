package com.example.androidtask.ui.custom;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.R;

public class LoadingVH extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;

    public LoadingVH(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loadmore_progress);
    }
}