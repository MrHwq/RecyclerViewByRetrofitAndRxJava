package com.hwqgooo.recyclerviewbyretrofit.presenter;

/**
 * Created by weiqiang on 2016/6/12.
 * 配合IBaseView使用
 */
public interface IBasePresenter {
    /**
     * 创建AsyncTask执行onCreate的耗时操作
     */
    void create();

    /**
     * 与销毁Task
     */
    void destory();
}
