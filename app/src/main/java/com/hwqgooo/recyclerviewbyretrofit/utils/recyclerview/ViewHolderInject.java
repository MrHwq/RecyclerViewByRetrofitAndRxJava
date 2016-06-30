package com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by weiqiang on 2016/6/6.
 */
public abstract class ViewHolderInject<T> extends RecyclerView.ViewHolder {
    public ViewHolderInject(View itemView) {
        super(itemView);
    }

    /**
     * 装载数据 <功能详细描述>
     *
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public abstract void loadData(T data, int position);
}
