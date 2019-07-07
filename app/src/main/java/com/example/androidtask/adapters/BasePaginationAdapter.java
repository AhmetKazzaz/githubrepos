package com.example.androidtask.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter class is responsible for changing the item type when new paginated data is being fetched from the API
 *
 * @param <T> : This is a generic object that represents the type of data that is used by the extending adapters
 */
public abstract class BasePaginationAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int LOADING = 2;
    static final int ITEM = 1;

    ArrayList<T> items = new ArrayList<>();
    private boolean isLoadingAdded;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);

    BasePaginationAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size() && isLoadingAdded) {
            return LOADING;
        }

        return ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return setViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Ignore binding for loading footer.
        if (isLoadingAdded && position == items.size()) {
            return;
        }

        onBindData(holder, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (isLoadingAdded) {
            return items.size() + 1;
        }

        return items.size();
    }


    private T getItem(int position) {
        return items.get(position);
    }

    private void add(T mc) {
        items.add(mc);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(List<T> mcList) {
        for (T mc : mcList) {
            add(mc);
        }
    }

    private void remove(T city) {
        int position = items.indexOf(city);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        removeLoadingFooter();

        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true;
            notifyItemInserted(items.size());
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;

            notifyItemRemoved(items.size());
        }
    }
}
