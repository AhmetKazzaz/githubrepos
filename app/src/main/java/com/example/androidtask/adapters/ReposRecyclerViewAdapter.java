package com.example.androidtask.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.R;
import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.ui.custom.LoadingVH;
import com.squareup.picasso.Picasso;

public class ReposRecyclerViewAdapter extends BasePaginationAdapter<GithubRepository> {

    private final OnRepoItemClickListener onRepoItemClickListener;
    private final OnRepoAvatarClickListener onRepoAvatarClickListener;


    public ReposRecyclerViewAdapter(OnRepoItemClickListener onItemClickedListener, OnRepoAvatarClickListener onRepoAvatarClickListener) {
        this.onRepoItemClickListener = onItemClickedListener;
        this.onRepoAvatarClickListener = onRepoAvatarClickListener;
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_recyclerview_item, parent, false);
                viewHolder = new ViewHolder(view);
                break;
            case LOADING:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    public void onBindData(RecyclerView.ViewHolder viewHolder, GithubRepository val,
                           int position) {
        if (getItemViewType(position) != LOADING) {
            ((ViewHolder) viewHolder).tvRepoName.setText(val.name);
            ((ViewHolder) viewHolder).tvUserName.setText(val.owner.login);
            if (val.owner.avatar != null)
                Picasso.get().load(val.owner.avatar).into(((ViewHolder) viewHolder).ivAvatar);

            ((ViewHolder) viewHolder).itemView.setOnClickListener(v -> {
                if (onRepoItemClickListener != null)
                    onRepoItemClickListener.onRepoItemClicked(val.id, val.owner.login);
            });

            ((ViewHolder) viewHolder).ivAvatar.setOnClickListener(v -> {
                if (onRepoAvatarClickListener != null)
                    onRepoAvatarClickListener.onRepoAvatarClicked(val.owner.login);
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvUserName, tvRepoName;

        ViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRepoName = itemView.findViewById(R.id.tvRepoName);
        }
    }

    public interface OnRepoItemClickListener {
        void onRepoItemClicked(int itemId, String itemOwnerLogin);
    }

    public interface OnRepoAvatarClickListener {
        void onRepoAvatarClicked(String name);
    }
}

