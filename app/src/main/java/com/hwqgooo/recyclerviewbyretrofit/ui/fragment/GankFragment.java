package com.hwqgooo.recyclerviewbyretrofit.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.model.adapter.GankAdapter;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Gank;
import com.hwqgooo.recyclerviewbyretrofit.presenter.gank.GankPresenter;
import com.hwqgooo.recyclerviewbyretrofit.presenter.gank.IGankPresenter;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.BaseRecyclerViewListener;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.OnRcvScrollListener;
import com.hwqgooo.recyclerviewbyretrofit.view.IBaseFragment;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by weiqiang on 2016/6/26.
 */
public class GankFragment extends BaseFragment implements IBaseFragment, SwipeRefreshLayout
        .OnRefreshListener, IGirlView<LinearLayoutManager, GankAdapter, Gank> {
    final String TAG = getClass().getSimpleName();
    String title;
    Unbinder unbinder;
    IGankPresenter presenter;
    GankAdapter gankAdapter;
    boolean isloading = false;
    @BindView(R.id.girllistview)
    RecyclerView girllistview;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public String getTitle() {
        return "GankDetail";
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return girllistview;
    }

    @Override
    public GankAdapter getAdapter() {
        return gankAdapter;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) girllistview.getLayoutManager();
    }

    @Override
    public void startRefresh() {
        Log.d(TAG, "startRefresh: ");
        swipeRefreshLayout.setRefreshing(true);
        isloading = true;
    }

    @Override
    public void stopRefresh() {
        Log.d(TAG, "stopRefresh: ");
        swipeRefreshLayout.setRefreshing(false);
        isloading = false;
    }

    @Override
    public void onItemClick(View view, Gank data) {
        GankWebFragment fragment = new GankWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", data.getGankDetail().getUrl());
        fragment.setArguments(bundle);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment.show(fm, "fragment_girl_photo");
    }

    @Override
    public Snackbar showSnackBar(String content) {
        return Snackbar.make(girllistview, content, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new GankPresenter(context, this);
        Log.d(TAG, "onAttach: " + presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + presenter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + presenter);
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        presenter.destory();
    }

    @Override
    public boolean restoreData() {
        return false;
    }

    @Override
    public boolean loadData() {
        presenter.create();
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.girlfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        Log.d(TAG, "onCreateView: " + presenter);
        onPreExecute();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + presenter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + presenter);
    }

    @Override
    public void onPreExecute() {
        setRecylerView();
        setSwipeRefreshLayout();
    }

    @Override
    public void doBackground() {
    }

    @Override
    public void onPostExecute() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isloading) {
            return;
        }
        startRefresh();
        presenter.refresh();
    }

    private void setRecylerView() {
        gankAdapter = new GankAdapter(context);
        girllistview.setAdapter(gankAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        girllistview.setLayoutManager(linearLayoutManager);
        girllistview.addOnItemTouchListener(new BaseRecyclerViewListener<GankAdapter.GankHolder>
                (girllistview) {
            @Override
            public void onItemClick(GankAdapter.GankHolder holder, int position) {
                GankFragment.this.onItemClick(holder.itemView, holder.gank);
            }
        });
        girllistview.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                                .getDisplayMetrics()));
                presenter.loadMore();
            }

            @Override
            public int setRestItem() {
                return 6;
            }
        });

        girllistview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).build
                ());
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置首次运行进度条刷新
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        //设置进度条颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color
                .holo_blue_dark, android.R.color.holo_orange_dark);
    }
}
