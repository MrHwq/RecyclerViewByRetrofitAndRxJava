package com.hwqgooo.recyclerviewbyretrofit.presenter.gank;

import com.hwqgooo.recyclerviewbyretrofit.presenter.IBasePresenter;

/**
 * Created by weiqiang on 2016/6/26.
 */
public interface IGankPresenter extends IBasePresenter {
    void restore();//使用现有数据，不刷新

    void refresh();

    void loadMore();
}
