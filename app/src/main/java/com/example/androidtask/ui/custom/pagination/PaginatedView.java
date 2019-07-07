package com.example.androidtask.ui.custom.pagination;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Interface for views to implement when pagination of data is needed
 */
public interface PaginatedView {

    /**
     * Animates a returned view based on dx, dy scroll
     *
     * @return: The view to animate
     */
    @Nullable
    View passOtherViewToAnimate();

    /**
     * gets called when there is more data to load
     */
    @Nullable
    void loadMoreItems();

    /**
     * Check if this is the last page in the list
     *
     * @return: returns true if last page, false if last page hasn't been reached yet
     */
    @Nullable
    boolean isLastPage();

    /**
     * @return: true if it should load, false if not
     */
    @Nullable
    boolean isLoading();

    @Nullable
    LinearLayoutManager passLinearLayoutManager();

    /**
     * Responsible for getting the scrollview to implement the scroll listener on
     *
     * @return: the passed scrollview
     */
    @NonNull
    View passScrollView();
}
