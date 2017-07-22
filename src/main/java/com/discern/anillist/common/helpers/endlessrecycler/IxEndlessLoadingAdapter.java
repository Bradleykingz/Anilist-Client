package com.discern.anillist.common.helpers.endlessrecycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


/**
 * @author ixsan
 */
public abstract class IxEndlessLoadingAdapter<T> extends IxRecyclerViewAdapter<T>{


    private final int VISIBLE_THRESHOLD = 5;

    private boolean mIsLoadingMore;
    private boolean mIsLoadingPrevious;

    private int firstVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private boolean enableLoadMore = true;

    public IxEndlessLoadingAdapter(RecyclerView rv, List<T> data, final RecyclerListener listener){
        super(rv, data, false, true); //with footer without header

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(!enableLoadMore) return;

                    totalItemCount = linearLayoutManager.getItemCount();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (mIsLoadingMore) {
                        if (totalItemCount > previousTotal) {
                            mIsLoadingMore = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!mIsLoadingMore && (totalItemCount - visibleItemCount)  <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                        //Reach last item

                        showFooter(true);
                        if (listener != null) {
                            listener.onLoadMore();
                        }
                        mIsLoadingMore = true;
                    }
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        //Endless Loading RecyclerView needs no HeaderVie. So, just ignore it.
        return null;
    }

    public void setEnableLoadMore(boolean enable){
        showFooter(false);
        this.enableLoadMore = enable;
    }

    /**
     * Load more listener functionality
     */
    public interface RecyclerListener {

        /**
         * Load more on bottom
         */
        void onLoadMore();

    }
}
