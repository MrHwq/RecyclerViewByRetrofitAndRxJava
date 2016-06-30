package com.hwqgooo.recyclerviewbyretrofit.presenter.girl;

import com.hwqgooo.recyclerviewbyretrofit.presenter.IBasePresenter;

/**
 * Created by weiqiang on 2016/6/10.
 */
public interface IGirlPresenter extends IBasePresenter {
    void restore();//使用现有数据，不刷新

    void refresh();

    void loadMore();
}
