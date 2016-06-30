package com.hwqgooo.recyclerviewbyretrofit.presenter.hfs;

import com.hwqgooo.recyclerviewbyretrofit.presenter.IBasePresenter;

/**
 * Created by weiqiang on 2016/6/13.
 */
public interface IHfsPresenter extends IBasePresenter {
    void restore();//使用现有数据，不刷新

    void refresh();

    void loadMore();
}