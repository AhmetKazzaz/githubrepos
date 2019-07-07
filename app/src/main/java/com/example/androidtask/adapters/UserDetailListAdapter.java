package com.example.androidtask.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.model.ui.UserCompleteData;
import com.example.androidtask.R;
import com.example.androidtask.ui.custom.LoadingVH;
import com.squareup.picasso.Picasso;

public class UserDetailListAdapter extends com.example.androidtask.adapters.BasePaginationAdapter<UserCompleteData> {

    private static final int DETAILS = 3;
    private static final int REPOS = 4;

    public UserDetailListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case DETAILS:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details_recyclerview_item, parent, false);
                viewHolder = new UserDetailsVH(v1);
                break;
            case REPOS:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_recyclerview_item, parent, false);
                viewHolder = new UserReposVH(v2);
                break;
            case LOADING:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
                viewHolder = new LoadingVH(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, UserCompleteData item, int position) {
        int type = getItemViewType(position);

        switch (type) {
            case REPOS:

                ((UserReposVH) holder).tvRepoName.setText(item.githubRepository.name);
                ((UserReposVH) holder).tvUserName.setText(item.githubRepository.owner.login);
                if (item.githubRepository.owner.avatar != null)
                    Picasso.get().load(item.githubRepository.owner.avatar).into(((UserReposVH) holder).ivAvatar);

                break;

            case DETAILS:

                if (item.owner.avatar != null)
                    Picasso.get().load(item.owner.avatar).into(((UserDetailsVH) holder).ivAvatar);
                ((UserDetailsVH) holder).tvUserEmail.setText(item.owner.email);
                ((UserDetailsVH) holder).tvUserName.setText(item.owner.name);

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // let parent decide type (loading, or item)
        int type = super.getItemViewType(position);

        // if it's not loading,
        // then we'll return our custom item type
        if (type == ITEM) {
            if (items.get(position).type == 1) {
                type = DETAILS;
            } else {
                type = REPOS;
            }
        }

        return type;
    }

    public class UserDetailsVH extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvUserName, tvUserEmail;

        UserDetailsVH(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
        }

    }

    public class UserReposVH extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvUserName, tvRepoName;

        UserReposVH(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRepoName = itemView.findViewById(R.id.tvRepoName);
        }
    }
}
