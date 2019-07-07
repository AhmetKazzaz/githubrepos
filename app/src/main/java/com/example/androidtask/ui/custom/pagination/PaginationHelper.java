package com.example.androidtask.ui.custom.pagination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * this class is a helper class that is responsible for implementing the scroll listener and animating extra passed views
 */
public class PaginationHelper {

    private WeakReference<PaginatedView> paginatedView;

    public PaginationHelper(@NonNull PaginatedView paginated) {
        this.paginatedView = new WeakReference<>(paginated);
        onScrollListener(paginated);
    }

    private boolean isFiltersBarHide = false;


    /**
     * This method checks for type of passedview, if it is indeed a scrollable view of type
     * Recyclerview (in this case: in this challenge project all data is dynamic)
     * then adds a scroll listener to it that checks invokes the loadMoreItems to fetch more data [Pagination]
     */
    private void onScrollListener(PaginatedView paginated) {

        if (paginated.passScrollView() instanceof RecyclerView) {
            ((RecyclerView) paginated.passScrollView()).addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    PaginatedView pagination = paginatedView.get();
                    if (pagination != null) {
                        int visibleItemCount = pagination.passLinearLayoutManager().getChildCount();
                        int totalItemCount = pagination.passLinearLayoutManager().getItemCount();
                        int firstVisibleItemPosition = pagination.passLinearLayoutManager().findFirstVisibleItemPosition();

                        if (!pagination.isLoading() && !pagination.isLastPage()) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                    && firstVisibleItemPosition >= 0) {
                                pagination.loadMoreItems();
                            }
                        }

                        if (dy > 0) {
                            animatePassedView(true, false);

                        } else {
                            animatePassedView(false, false);
                        }
                    }
                }
            });
        }
    }


    /**
     * Animates a view up and down
     *
     * @param hide:   Indicated whether the view should be animated to be shown or hidden
     * @param isFast: true if the animation should be fast, false if should be normal
     */
    public void animatePassedView(final boolean hide, boolean isFast) {
        if (paginatedView.get() == null) return;

        if (paginatedView.get().passOtherViewToAnimate() == null) return;

        if (isFiltersBarHide && hide || !isFiltersBarHide && !hide) return;
        isFiltersBarHide = hide;
        int moveY = hide ? -(2 * paginatedView.get().passOtherViewToAnimate().getHeight()) : 0;
        paginatedView.get().passOtherViewToAnimate().animate().translationY(moveY).setStartDelay(isFast ? 0 : 100).setDuration(isFast ? 100 : 300).start();
    }

    /**
     * method for decoding a header from Github "link" header.
     * to get the required data for pagination
     *
     * @param linkHeader: a header passed from view when a response has been recieved from the Github API
     * @return: a string which is the last page url. returns null if last page has been reached
     */
    public String linkDecode(String linkHeader) {
        String META_REL = "rel";
        String META_LAST = "last";

        String last = null;
        if (linkHeader != null) {
            String[] links = linkHeader.split(",");
            for (String link : links) {
                String[] segments = link.split(";");
                if (segments.length < 2)
                    continue;

                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                    if (rel.length < 2 || !META_REL.equals(rel[0]))
                        continue;

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length() - 1);

                    if (META_LAST.equals(relValue))
                        last = linkPart;
                }
            }
        }
        return last;
    }

}
