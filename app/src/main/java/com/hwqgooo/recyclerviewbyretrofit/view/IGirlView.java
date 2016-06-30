package com.hwqgooo.recyclerviewbyretrofit.view;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.BaseRecyclerViewAdapter;

/**
 * Created by weiqiang on 2016/6/10.
 */
public interface IGirlView<GirlLayoutManagerType extends RecyclerView.LayoutManager, GirlAdapterType
        extends BaseRecyclerViewAdapter, V> extends IBaseView {
    RecyclerView getRecyclerView();

    GirlAdapterType getAdapter();

    GirlLayoutManagerType getLayoutManager();

    void startRefresh();

    void stopRefresh();

    void onItemClick(View view, V data);

    Snackbar showSnackBar(String content);
}
