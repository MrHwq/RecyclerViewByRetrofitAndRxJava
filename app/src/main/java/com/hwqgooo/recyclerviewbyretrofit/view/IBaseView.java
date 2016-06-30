package com.hwqgooo.recyclerviewbyretrofit.view;

/**
 * Created by weiqiang on 2016/6/12.
 * 配合IBasePresenter使用
 */
public interface IBaseView {

    void onPreExecute();

    void doBackground();

    void onPostExecute();
}
