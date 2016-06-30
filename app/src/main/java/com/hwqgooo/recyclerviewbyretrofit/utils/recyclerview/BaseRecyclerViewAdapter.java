package com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by weiqiang on 2016/6/6.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolderInject> {
    public final LayoutInflater mLayoutInflater;
    public final Context context;
    public List<T> mData;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderInject<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return getNewHolder(mLayoutInflater, parent, viewType);
    }

    public abstract ViewHolderInject<T> getNewHolder(LayoutInflater layoutInflater, ViewGroup
            parent, int viewType);

    @Override
    public void onBindViewHolder(ViewHolderInject holder, int position) {
        holder.loadData(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<T> data) {
        if (mData == null) {
            mData = new LinkedList<T>();
        }
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(T data) {
        if (mData == null) {
            mData = new LinkedList<T>();
        }
        mData.clear();
        if (data != null) {
            mData.add(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<T>();
        }
        int pos = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(pos, data.size());
    }

    public void addData(T data) {
        if (mData == null) {
            mData = new ArrayList<T>();
        }

        int pos = mData.size();
        mData.add(data);
        notifyItemInserted(pos);
    }

    public void removeAllData() {
        if (mData == null) {
            return;
        }
        mData.clear();
        notifyDataSetChanged();
    }
}
