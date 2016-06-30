package com.hwqgooo.recyclerviewbyretrofit.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.model.adapter.GirlAdapter;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;
import com.hwqgooo.recyclerviewbyretrofit.presenter.hfs.HfsPresenter;
import com.hwqgooo.recyclerviewbyretrofit.presenter.hfs.IHfsPresenter;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.BaseRecyclerViewListener;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.OnRcvScrollListener;
import com.hwqgooo.recyclerviewbyretrofit.view.IBaseFragment;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by weiqiang on 2016/6/13.
 */
public class HfsFragment extends BaseFragment implements IBaseFragment, SwipeRefreshLayout
        .OnRefreshListener, IGirlView<StaggeredGridLayoutManager, GirlAdapter, Girl> {
    final String TAG = getClass().getSimpleName();

    String title = "Hfs";
    Unbinder unbinder;
    IHfsPresenter presenter;
    GirlAdapter girlAdapter;
    boolean isloading = false;

    @BindView(R.id.girllistview)
    RecyclerView girllistview;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.urledit)
    AppCompatEditText urledit;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new HfsPresenter(context, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        presenter.destory();
    }

    @Override
    public boolean loadData() {
        presenter.create();
        return false;
    }

    @Override
    public boolean restoreData() {
        presenter.restore();
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.hfsfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        onPreExecute();
        return view;
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
        Log.d(TAG, "onPostExecute: ");
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
        girlAdapter = new GirlAdapter(context);
        girllistview.setAdapter(girlAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        girllistview.setLayoutManager(gridLayoutManager);
        girllistview.addOnItemTouchListener(new BaseRecyclerViewListener<GirlAdapter.GirlHolder>
                (girllistview) {
            @Override
            public void onItemClick(GirlAdapter.GirlHolder holder, int position) {
                HfsFragment.this.onItemClick(holder.itemView, holder.girl);
            }
        });
        girllistview.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                                .getDisplayMetrics()));
                startRefresh();
                presenter.loadMore();
            }

            @Override
            public int setRestItem() {
                return 6;
            }
        });
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

    @Override
    public String getTitle() {
        return title;
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
    public GirlAdapter getAdapter() {
        return girlAdapter;
    }

    @Override
    public StaggeredGridLayoutManager getLayoutManager() {
        return (StaggeredGridLayoutManager) girllistview.getLayoutManager();
    }

    @Override
    public void startRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        isloading = true;
    }

    @Override
    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        isloading = false;
    }

    @Override
    public void onItemClick(View view, Girl data) {
        GirlPhotoFragment fragment = new GirlPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("girl", data);
        fragment.setArguments(bundle);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment.show(fm, "fragment_girl_photo");
    }

    @Override
    public Snackbar showSnackBar(String content) {
        return Snackbar.make(girllistview, content, Snackbar.LENGTH_LONG);
    }
}
